package com.openclassrooms.realestatemanager.fragments;

import static com.openclassrooms.realestatemanager.utils.Utils.convertAddressToGpsCoordinates;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.controllers.MainActivity;
import com.openclassrooms.realestatemanager.database.model.RealEstate;
import com.openclassrooms.realestatemanager.database.model.geocoding_api.ResultsItem;
import com.openclassrooms.realestatemanager.databinding.FragmentMapsBinding;
import com.openclassrooms.realestatemanager.utils.OnGeocodingApiReceivedCallback;

import java.util.List;
import java.util.Objects;

public class RealEstateMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener, GoogleMap.OnMarkerClickListener, OnGeocodingApiReceivedCallback {

    public FragmentMapsBinding binding;
    private List<RealEstate> mRealEstates;
    private Context context;

    private GoogleMap googleMap;
    private Location currentLocation;

    public OnRealEstateOnMapListener onRealEstateOnMapListener;


    public static RealEstateMapFragment newInstance(
            Context context, List<RealEstate> realEstates, OnRealEstateOnMapListener onRealEstateOnMapListener) {

        RealEstateMapFragment realEstateMapFragment = new RealEstateMapFragment();
        realEstateMapFragment.context = context;
        realEstateMapFragment.mRealEstates = realEstates;
        realEstateMapFragment.onRealEstateOnMapListener = onRealEstateOnMapListener;
        return realEstateMapFragment;
    }


    private void initData() {

        googleMap = null;
        currentLocation = ((MainActivity) requireContext()).currentLocation;

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(RealEstateMapFragment.this);
    }


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMapsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.initData();
        ((MainActivity) requireContext()).isMapEnabled = true;
    }


    public void updateMarkers(List<RealEstate> realEstates) {
        mRealEstates = realEstates;
        if (this.googleMap != null) {
            this.googleMap.clear();
        }
        initData();
    }


    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        String markerTag = Objects.requireNonNull(marker.getTag()).toString();
        long id = Long.parseLong(markerTag);
        onRealEstateOnMapListener.onRealEstateOnMapClick(id);
        return false;
    }

    private boolean isCurrentLocationChanged() {
        return currentLocation.getLongitude() != ((MainActivity) requireContext()).currentLocation.getLongitude() || currentLocation.getLatitude() != ((MainActivity) requireContext()).currentLocation.getLatitude();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        if (isCurrentLocationChanged()) {

            currentLocation.setLongitude(((MainActivity) requireContext()).currentLocation.getLongitude());

            currentLocation.setLatitude(((MainActivity) requireContext()).currentLocation.getLatitude());
        }
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(getActivity(), "Vous êtes ici", Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;

        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        this.googleMap.setOnMyLocationButtonClickListener(this);
        this.googleMap.setOnMyLocationClickListener(this);
        this.googleMap.getUiSettings().setZoomControlsEnabled(true);
        this.googleMap.getUiSettings().setMyLocationButtonEnabled(true);

        if (this.context != null) {

            if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            this.googleMap.setMyLocationEnabled(true);
        }


        if (currentLocation != null) {
            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

            this.googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        } else {
            currentLocation = ((MainActivity) requireContext()).currentLocation;

        }
        this.googleMap.setOnMarkerClickListener(this);

        putMarkerOnAddress();


    }

    private void putMarkerOnAddress() {
        if (mRealEstates != null) {
            for (RealEstate realEstate : mRealEstates) {
                convertAddressToGpsCoordinates(this, context, realEstate, "");
            }
        }
    }


    @Override
    public void onGeocodingApiReceivedCallback(List<ResultsItem> addresses, RealEstate realEstate, String action) {
        for (ResultsItem address : addresses) {

            double latitude = address.getGeometry().getLocation().getLat();
            double longitude = address.getGeometry().getLocation().getLng();

            LatLng location = new LatLng(latitude, longitude);
            MarkerOptions markerOptions = new MarkerOptions().position(location);
            // Ajouter le marqueur à la carte
            markerOptions.title(realEstate.getAddress());

            Marker marker = Objects.requireNonNull(this.googleMap.addMarker(markerOptions));
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            marker.setFlat(true);
            marker.setTag(realEstate.getId());
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            currentLocation = savedInstanceState.getParcelable("currentLocation");
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (this.googleMap != null) {
            outState.putParcelable("currentLocation", currentLocation);
        }
    }

    public interface OnRealEstateOnMapListener {
        void onRealEstateOnMapClick(long id);
    }
}
