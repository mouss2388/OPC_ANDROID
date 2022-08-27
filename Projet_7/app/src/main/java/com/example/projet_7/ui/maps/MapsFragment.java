package com.example.projet_7.ui.maps;

import static com.example.projet_7.ui.MainActivity.placesClient;
import static com.example.projet_7.utils.Utils.getLatLngForMatrixApi;
import static com.example.projet_7.utils.Utils.isSearchBoxLengthAtleast3;
import static com.example.projet_7.utils.Utils.startDetailActivity;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.projet_7.R;
import com.example.projet_7.databinding.FragmentMapsBinding;
import com.example.projet_7.model.Restaurant;
import com.example.projet_7.model.User;
import com.example.projet_7.model.matrix_api.ElementsItem;
import com.example.projet_7.model.matrix_api.RowsItem;
import com.example.projet_7.ui.MainActivity;
import com.example.projet_7.utils.OnMatrixApiListReceivedCallback;
import com.example.projet_7.viewModel.RestaurantViewModel;
import com.example.projet_7.viewModel.WorkMateViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("MissingPermission")
public class MapsFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener, GoogleMap.OnMarkerClickListener, OnMatrixApiListReceivedCallback {

    private FragmentMapsBinding binding;
    private final Context context;
    private SupportMapFragment supportMapFragment;

    private GoogleMap googleMap;

    private Location currentLocation;

    private final ArrayList<Restaurant> restaurants = new ArrayList<>();
    RestaurantViewModel restaurantViewModel;
    WorkMateViewModel workMateViewModel;

    public MapsFragment(Context context) {
        this.context = context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMapsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.initViewModel();
        this.initData();
    }

    private void initViewModel() {
        restaurantViewModel = new ViewModelProvider(requireActivity()).get(RestaurantViewModel.class);
        workMateViewModel = new ViewModelProvider(requireActivity()).get(WorkMateViewModel.class);
    }

    private void initData() {

        googleMap = null;
        currentLocation = ((MainActivity) requireContext()).currentLocation;
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.maps);
        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(MapsFragment.this);
    }

    @Override
    public boolean onMyLocationButtonClick() {

        Toast.makeText(getActivity(), "MainAcitivty " + ((MainActivity) requireContext()).currentLocation.getLongitude(), Toast.LENGTH_LONG)
                .show();
        Toast.makeText(getActivity(), "Maps Fragment " + currentLocation.getLongitude(), Toast.LENGTH_LONG)
                .show();
        if (isCuruentLocationChanged()) {
            currentLocation.setLongitude(((MainActivity) requireContext()).currentLocation.getLongitude());
            currentLocation.setLatitude(((MainActivity) requireContext()).currentLocation.getLatitude());
            restaurantViewModel.getRestaurants(placesClient);
            getRestaurantsFromLocationAndPrediction();
            Toast.makeText(getActivity(), "currentLocation update ", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(getActivity(), "currentLocation not update ", Toast.LENGTH_SHORT)
                    .show();

        }

        return false;
    }

    private boolean isCuruentLocationChanged() {
        return currentLocation.getLongitude() != ((MainActivity) requireContext()).currentLocation.getLongitude() || currentLocation.getLatitude() != ((MainActivity) requireContext()).currentLocation.getLatitude();
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(getActivity(), "Vous êtes ici:\n" + location.getLatitude() + " " + location.getLongitude(), Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        this.googleMap = googleMap;
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        this.googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this.context, R.raw.style_json));
        this.googleMap.setOnMyLocationButtonClickListener(this);
        this.googleMap.setOnMyLocationClickListener(this);
        this.googleMap.getUiSettings().setZoomControlsEnabled(true);
        this.googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        this.googleMap.setMyLocationEnabled(true);

        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        this.googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 19));
        this.googleMap.setOnMarkerClickListener(this);
        getRestaurantsFromLocationAndPrediction();
    }


    private void getRestaurantsFromLocationAndPrediction() {
        getRestaurantsByPrediction();
        getRestaurantsAroundMe();
    }

    private void getRestaurantsByPrediction() {
        restaurantViewModel.getLiveDataRestaurantsPrediction().observe(getViewLifecycleOwner(), mRestaurants -> {
            this.googleMap.clear();

            restaurants.clear();
            restaurants.addAll(mRestaurants);
            if (isSearchBoxLengthAtleast3(getContext())) {
                checkRestaurantBooked(restaurants);
            }
        });
    }


    private void getRestaurantsAroundMe() {
        restaurantViewModel.getLiveData().

                observe(getViewLifecycleOwner(), mRestaurants ->
                {
                    this.googleMap.clear();
                    restaurants.clear();
                    restaurants.addAll(mRestaurants);

                    if (!isSearchBoxLengthAtleast3(getContext())) {
                        checkRestaurantBooked(restaurants);
                    }
                });
    }

    private void checkRestaurantBooked(ArrayList<Restaurant> restaurants) {

        workMateViewModel.getLiveData().observe(getViewLifecycleOwner(), mWorkmates -> {
            boolean booked;

            for (Restaurant restaurant : restaurants) {
                booked = false;
                for (User workmate : mWorkmates) {
                    if (restaurant.getId().equals(workmate.getRestaurantBookedId())) {
                        booked = true;
                        addMarkerColored(restaurant, booked);
                        break;
                    }
                }
                if (!booked) {
                    addMarkerColored(restaurant, booked);
                }
            }
        });
    }

    private void addMarkerColored(Restaurant restaurant, boolean booked) {

        Objects.requireNonNull(this.googleMap.addMarker(new MarkerOptions()
                .position(restaurant.getLatLng())
                .icon(BitmapDescriptorFactory.fromResource(booked ? R.drawable.ic_restaurant_booked_24 : R.drawable.ic_restaurant_unbooked_24))
                .title(restaurant.getName())
                .snippet(restaurant.getAddress())
                .flat(true))).setTag(restaurant.getId());
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {

        Location destination = new Location("");
        destination.setLatitude(marker.getPosition().latitude);
        destination.setLongitude(marker.getPosition().longitude);

        startDetailActivity(getContext(), marker.getTag().toString());

        StringBuilder currentLocationCoord = getLatLngForMatrixApi(currentLocation);
        StringBuilder destinationCoord = getLatLngForMatrixApi(marker.getPosition());

        //getDistanceBetween(this, getContext(), currentLocationCoord, destinationCoord);

        return false;
    }

    @Override
    public void onMatrixApiListReceivedCallback(List<RowsItem> rowsItem, String id) {

        ElementsItem matrixItem = rowsItem.get(0).getElements().get(0);
        getInfoDistanceRestaurant(matrixItem);
    }


    void getInfoDistanceRestaurant(ElementsItem matrixItem) {
        Toast.makeText(context, "distance:" + matrixItem.getDistance().getText(), Toast.LENGTH_SHORT).show();
        Toast.makeText(context, "duree:" + matrixItem.getDuration().getText(), Toast.LENGTH_SHORT).show();
    }

}
