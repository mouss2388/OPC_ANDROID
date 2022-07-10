package com.example.projet_7.ui.maps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.projet_7.R;
import com.example.projet_7.databinding.FragmentMapsBinding;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;

@SuppressWarnings("MissingPermission")
public class MapsFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener {

    private FragmentMapsBinding binding;
    private final Context context;
    private SupportMapFragment supportMapFragment;

    private String[] PERMISSIONS;
    private ActivityResultLauncher<String[]> requestPermissionLauncher;

    private Location currentLocation;
    private FusedLocationProviderClient mFusedLocationClient;

    private LocationRequest mLocationRequest = null;
    private LocationCallback mLocationCallback = null;

    private  GoogleMap googleMap;


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

        this.initData();
        this.handleResponsePermissionsRequest();

        if (this.isAndroidVersionBelowMarshmallow()) {
            fetchLocation();
        } else {
            checkPermissions();
        }
    }

    private void initData() {

        PERMISSIONS = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
        };

        currentLocation = null;
        requestPermissionLauncher = null;
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

    private void handleResponsePermissionsRequest() {

        requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), permissions -> {

                    boolean areAllGranted = true;

                    for (Boolean b : permissions.values()) {
                        areAllGranted = areAllGranted && b;
                    }

                    if (areAllGranted) {
                        checkPermissions();
                    } else {
                        Toast.makeText(context, "You can't use application normally", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean isAndroidVersionBelowMarshmallow() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M;
    }

    private void fetchLocation() {

        Task<Location> task = mFusedLocationClient.getLastLocation();
        task.addOnSuccessListener(location -> {
            if (location != null) {
                currentLocation = location;
                Toast.makeText(getActivity(), currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();

                supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.maps);
                assert supportMapFragment != null;
                supportMapFragment.getMapAsync(MapsFragment.this);

            }else {
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

    private void checkPermissions() {

        if (hasPermissions()) {
            fetchLocation();
        } else {
            askLocationPermissions();
        }
    }

    private boolean hasPermissions() {

        if (context != null && PERMISSIONS != null) {
            for (String permission : PERMISSIONS) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void askLocationPermissions() {
        requestPermissionLauncher.launch(PERMISSIONS);
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
        this.googleMap.setOnMyLocationButtonClickListener(this);
        this.googleMap.setOnMyLocationClickListener(this);
        this.googleMap.getUiSettings().setZoomControlsEnabled(true);
        this.googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        this.googleMap.setMyLocationEnabled(true);

        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        this.googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
    }
}