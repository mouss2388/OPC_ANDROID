package com.example.projet_7.ui.maps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.projet_7.R;
import com.example.projet_7.databinding.FragmentMapsBinding;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

@SuppressWarnings("MissingPermission")
public class MapsFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener  {

    private FragmentMapsBinding binding;
    private Context context;
    private SupportMapFragment mapFragment;

    private String[] PERMISSIONS;
    private ActivityResultLauncher<String[]> requestPermissionLauncher = null;




    public MapsFragment(Context context) {
        this.context = context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMapsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.initData();
        this.handleResponsePermissionsRequest();

        if (this.isAndroidVersionBelowMarshmallow()) {
            Toast.makeText(context, "you already have permissions", Toast.LENGTH_SHORT).show();
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

    private void checkPermissions() {

        if (hasPermissions()) {
            ///Start location
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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setOnMyLocationButtonClickListener(this);
        googleMap.setOnMyLocationClickListener(this);
    }
}