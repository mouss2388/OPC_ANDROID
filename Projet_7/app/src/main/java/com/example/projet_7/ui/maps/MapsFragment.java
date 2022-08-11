package com.example.projet_7.ui.maps;

import static com.example.projet_7.utils.Utils.getLatLngForMatrixApi;
import static com.example.projet_7.utils.Utils.startDetailActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
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
import com.example.projet_7.model.matrix_api.ElementsItem;
import com.example.projet_7.model.matrix_api.RowsItem;
import com.example.projet_7.ui.DetailActivity;
import com.example.projet_7.ui.MainActivity;
import com.example.projet_7.utils.OnMatrixApiListReceivedCallback;
import com.example.projet_7.viewModel.RestaurantViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("MissingPermission")
public class MapsFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener, GoogleMap.OnMarkerClickListener, OnMatrixApiListReceivedCallback {

    private FragmentMapsBinding binding;
    private final Context context;
    private SupportMapFragment supportMapFragment;

    private Location currentLocation;
    private FusedLocationProviderClient mFusedLocationClient;

    private LocationRequest mLocationRequest = null;
    private LocationCallback mLocationCallback = null;

    private GoogleMap googleMap;

    private final ArrayList<Restaurant> restaurants = new ArrayList<>();
    RestaurantViewModel restaurantViewModel;

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
        fetchLocation();
    }

    private void initViewModel() {
        restaurantViewModel = new ViewModelProvider(requireActivity()).get(RestaurantViewModel.class);
    }

    private void initData() {
        currentLocation = null;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.context.getApplicationContext());
        mLocationRequest = null;
        mLocationCallback = null;
        googleMap = null;

        initCallBackLocation();
        createLocationRequest();
    }

    private void initCallBackLocation() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                currentLocation = locationResult.getLastLocation();
            }
        };
    }

    private void createLocationRequest() {

        long LOCATION_REQUEST_INTERVAL = 1000L;

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .setInterval(LOCATION_REQUEST_INTERVAL);
    }

    private void fetchLocation() {

        Task<Location> task = mFusedLocationClient.getLastLocation();
        task.addOnSuccessListener(location -> {
            if (location != null) {
                currentLocation = location;

                supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.maps);
                assert supportMapFragment != null;
                supportMapFragment.getMapAsync(MapsFragment.this);
            } else {
                requestLocationUpdate();
                fetchLocation();
            }
        });
    }

    private void requestLocationUpdate() {

        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest,
                mLocationCallback,
                Looper.myLooper()
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        this.requestLocationUpdate();
    }

    @Override
    public void onPause() {
        super.onPause();
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    @Override
    public void onStop() {
        super.onStop();
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }


    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(getActivity(), "onMyLocationButtonClick clicked", Toast.LENGTH_SHORT)
                .show();
        addMarkerToRestaurants();
        restaurantViewModel.getRestaurants(MainActivity.placesClient);
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(getActivity(), " onMyLocationClick Current location:\n" + location, Toast.LENGTH_LONG)
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
        addMarkerToRestaurants();
    }


    private void addMarkerToRestaurants() {

        //TODO get workMates here and check restaurantBookedId
        restaurantViewModel.getLiveData().observe(getViewLifecycleOwner(), mRestaurants -> {
            restaurants.clear();
            restaurants.addAll(mRestaurants);

            if (restaurants.size() > 0) {

                for (Restaurant restaurant : restaurants) {

                    Objects.requireNonNull(this.googleMap.addMarker(new MarkerOptions()
                            .position(restaurant.getLatLng())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_restaurant_unbooked_24))
                            .title(restaurant.getName())
                            .snippet(restaurant.getAddress())
                            .flat(true))).setTag(restaurant.getId());
                }

            }
        });


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
    public void onMatrixApiListReceivedCallback(List<RowsItem> rowsItem) {

        ElementsItem matrixItem = rowsItem.get(0).getElements().get(0);
        getInfoDistanceRestaurant(matrixItem);
    }


    void getInfoDistanceRestaurant(ElementsItem matrixItem) {
        Toast.makeText(context, "distance:" + matrixItem.getDistance().getText(), Toast.LENGTH_SHORT).show();
        Toast.makeText(context, "duree:" + matrixItem.getDuration().getText(), Toast.LENGTH_SHORT).show();
    }

}
