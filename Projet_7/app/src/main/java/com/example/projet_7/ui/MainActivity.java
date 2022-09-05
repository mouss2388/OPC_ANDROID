package com.example.projet_7.ui;

import static com.example.projet_7.utils.Utils.startDetailActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.projet_7.BuildConfig;
import com.example.projet_7.R;
import com.example.projet_7.databinding.ActivityMainBinding;
import com.example.projet_7.manager.UserManager;
import com.example.projet_7.model.User;
import com.example.projet_7.ui.maps.MapsFragment;
import com.example.projet_7.ui.restaurants.RestaurantsFragment;
import com.example.projet_7.ui.workmates.WorkmatesFragment;
import com.example.projet_7.utils.Utils;
import com.example.projet_7.viewModel.RestaurantViewModel;
import com.example.projet_7.viewModel.WorkMateViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

@SuppressWarnings("MissingPermission")
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding binding;
    private final UserManager userManager = UserManager.getInstance();

    RestaurantViewModel restaurantViewModel;
    WorkMateViewModel workMateViewModel;
    public static PlacesClient placesClient;
    private String[] PERMISSIONS;
    private ActivityResultLauncher<String[]> requestPermissionLauncher;

    public FusedLocationProviderClient mFusedLocationClient;
    public Location currentLocation;
    public String querySearchView;

    private LocationRequest mLocationRequest = null;
    private LocationCallback mLocationCallback = null;

    private final Handler handler = new Handler();
    private final int TRIGGER_SEARCH_RESTAURANTS = 3;

    public static StringBuilder restaurantBookedInfo = new StringBuilder().append("");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        this.initData();
        this.handleResponsePermissionsRequest();

        if (this.isAndroidVersionBelowMarshmallow()) {
            this.initViewModel();
            fetchLocation();
        } else {
            checkPermissions();
        }

        this.showSnackBarLogin();
        this.configureMenu();
        this.configureBottomNav();
        this.updateMenuWithUserData();

    }

    private void initViewModel() {
        Places.initialize(this, BuildConfig.API_KEY);
        placesClient = Places.createClient(this);

        restaurantViewModel = new ViewModelProvider(this).get(RestaurantViewModel.class);

        workMateViewModel = new ViewModelProvider(this).get(WorkMateViewModel.class);

        restaurantViewModel.getRestaurantsAroundMe(placesClient);
        workMateViewModel.getWorkMates();

        getDataForNotification();
    }

    private void getDataForNotification() {

        userManager.getUserData().addOnSuccessListener(user -> {

            String restaurantBookedId = user.getRestaurantBookedId();
            if (!Objects.equals(restaurantBookedId, "")) {

                restaurantViewModel.getLiveDataRestaurantDetail().observe(this, restaurant -> {


                    restaurantBookedInfo.append(this.getResources().getString(R.string.you_booked_at))
                            .append(restaurant.getName())
                            .append("\n")
                            .append(restaurant.getAddress())
                            .append("\n");

                    workMateViewModel.getLiveDataUsersWhoHasBooked().observe(this, workmates -> {

                        restaurantBookedInfo.append(this.getResources().getString(R.string.there_will_be)).append("\n");
                        for (User workmate : workmates) {
                            restaurantBookedInfo.append("- ")
                                    .append(workmate.getUsername())
                                    .append("\n");
                        }
                    });
                    workMateViewModel.getWorkmatesBookedRestaurant(restaurantBookedId);
                });
                restaurantViewModel.getDetailsRestaurant(MainActivity.placesClient, restaurantBookedId);
            }
        });

    }

    private void initData() {
        PERMISSIONS = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
        };

        requestPermissionLauncher = null;
        currentLocation = null;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getBaseContext());
        querySearchView = "";
        initSearchBoxListener();
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
                        Toast.makeText(this, getString(R.string.info_must_accept_permission), Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
    }

    private void fetchLocation() {

        Task<Location> task = mFusedLocationClient.getLastLocation();
        task.addOnSuccessListener(location -> {
            if (location != null) {
                currentLocation = location;
                showMapFragment();
            } else {
                requestLocationUpdate();
                fetchLocation();
            }
        });
    }

    public void requestLocationUpdate() {

        initCallBackLocation();
        createLocationRequest();

        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest,
                mLocationCallback,
                Looper.myLooper()
        );
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

    private boolean isAndroidVersionBelowMarshmallow() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M;
    }

    private void checkPermissions() {

        if (hasPermissions()) {
            this.initViewModel();
            fetchLocation();
        } else {
            askLocationPermissions();
        }
    }

    private boolean hasPermissions() {

        if (getBaseContext() != null && PERMISSIONS != null) {
            for (String permission : PERMISSIONS) {
                if (ContextCompat.checkSelfPermission(getBaseContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void showMapFragment() {
        replaceFragment(new MapsFragment(getBaseContext()));
    }

    private void askLocationPermissions() {
        requestPermissionLauncher.launch(PERMISSIONS);
    }

    private void showSnackBarLogin() {
        Utils.showSnackBar(binding.mainLayout, getString(R.string.snackbar_msg_login_success));
    }

    private void configureMenu() {

        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();
    }


    private void configureToolBar() {
        binding.activityMainToolbar.setTitle(R.string.app_title);
        setSupportActionBar(binding.activityMainToolbar);
    }

    private void configureDrawerLayout() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.mainLayout, binding.activityMainToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.mainLayout.addDrawerListener(toggle);
        toggle.syncState();
    }


    private void configureNavigationView() {
        binding.activityMainNavView.setNavigationItemSelectedListener(this);
    }


    private void configureBottomNav() {
        this.setupListenerBottomNav();
    }

    private void setupListenerBottomNav() {

        binding.bottomNavView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.navigation_maps) {
                binding.searchView.setQueryHint(getResources().getString(R.string.search_restaurants));
                checkPermissions();

            } else if (item.getItemId() == R.id.navigation_restaurants) {
                replaceFragment(new RestaurantsFragment());
                binding.searchView.setQueryHint(getResources().getString(R.string.search_restaurants));
            } else {
                replaceFragment(new WorkmatesFragment());
                binding.searchView.setQueryHint(getResources().getString(R.string.search_workmates));
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.activity_main_frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_Item_1) {
            userManager.getUserData().addOnSuccessListener(user -> {

                String restaurantBooked = user.getRestaurantBookedId();
                startDetailActivity(this, restaurantBooked);
            });

        } else if (id == R.id.menu_Item_2) {
            Dialog dialog = configDialogSetting();
            setupListenerDialogSettings(dialog);

        } else if (id == R.id.menu_Item_3) {
            userManager.signOut(this).addOnSuccessListener(aVoid -> {
                finish();
                Toast.makeText(this, getString(R.string.msg_you_are_log_out), Toast.LENGTH_SHORT).show();
            });
        }

        binding.mainLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    private Dialog configDialogSetting() {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.settings_layout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return dialog;
    }

    private void setupListenerDialogSettings(Dialog dialog) {

        dialog.show();
        ImageButton close = dialog.findViewById(R.id.close_Settings);
        Button deleteAccount = dialog.findViewById(R.id.remove_Account);

        deleteAccount.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setMessage(R.string.popup_message_confirmation_delete_account)
                .setPositiveButton(R.string.popup_message_choice_yes, (dialogInterface, i) ->
                        userManager.deleteUser(MainActivity.this)
                                .addOnSuccessListener(aVoid -> {
                                            dialog.dismiss();
                                            finish();
                                            Toast.makeText(this, getString(R.string.msg_acount_is_removed), Toast.LENGTH_SHORT).show();

                                        }
                                )
                )
                .setNegativeButton(R.string.popup_message_choice_no, null)
                .show());

        close.setOnClickListener(v -> dialog.dismiss());
    }

    private void updateMenuWithUserData() {

        if (userManager.isCurrentUserLogged()) {

            FirebaseUser user = userManager.getCurrentUser();
            if (user.getPhotoUrl() != null) {
                setProfilePicture(user.getPhotoUrl());
            }
            setTextUserData(user);
            getUserData();
        }
    }

    private void setProfilePicture(Uri profilePictureUrl) {

        Glide.with(this)
                .load(profilePictureUrl)
                .apply(RequestOptions.circleCropTransform())
                .into((ImageView) accessMenuHeaderInfo().findViewById(R.id.user_Picture));
    }

    private View accessMenuHeaderInfo() {
        return binding.activityMainNavView.getHeaderView(0);
    }

    private void setTextUserData(FirebaseUser user) {


        String userEmail = TextUtils.isEmpty(user.getEmail()) ? getString(R.string.info_no_email_found) : user.getEmail();

        TextView userEmailTv = accessMenuHeaderInfo().findViewById(R.id.user_Email);
        userEmailTv.setText(userEmail);

        String username = TextUtils.isEmpty(user.getDisplayName()) ? getString(R.string.info_no_username_found) : user.getDisplayName();

        TextView usernameTv = accessMenuHeaderInfo().findViewById(R.id.user_Name);
        usernameTv.setText(username);

    }

    private void getUserData() {
        userManager.getUserData().addOnSuccessListener(user -> {
            // Set the data with the user information
            if (user != null) {
                String username = TextUtils.isEmpty(user.getUsername()) ? getString(R.string.info_no_username_found) : user.getUsername();
                TextView usernameTv = accessMenuHeaderInfo().findViewById(R.id.user_Name);

                usernameTv.setText(username);

                String urlPhoto = user.getUrlPicture();
                if (urlPhoto != null) {
                    Uri photo = Uri.parse(user.getUrlPicture());
                    setProfilePicture(photo);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        // 5 - Handle back click to close menu
        if (binding.mainLayout.isDrawerOpen(GravityCompat.START)) {
            binding.mainLayout.closeDrawer(GravityCompat.START);
        } else {
            Utils.showSnackBar(binding.mainLayout, getString(R.string.snackbar_log_out));
        }
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
    public void onDestroy() {
        super.onDestroy();
        binding = null;
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    private void initSearchBoxListener() {

        binding.searchView.setQueryHint(getString(R.string.search_restaurants));

        binding.searchView.setOnCloseListener(() -> {

            if (isWorkmatesBottomNavigationSelected()) {

                if (!querySearchView.isEmpty()) {
                    workMateViewModel.getWorkMates();
                }
            } else {
                restaurantViewModel.getRestaurantsAroundMe(placesClient);
            }

            querySearchView = "";
            return false;
        });


        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                querySearchView = newText;
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(() -> {

                    if (isWorkmatesBottomNavigationSelected()) {

                        getWorkmatesByQueryOrDefault(querySearchView.isEmpty());

                    } else {

                        if (querySearchView.length() >= TRIGGER_SEARCH_RESTAURANTS) {
                            restaurantViewModel.getRestaurantsByPrediction(currentLocation, querySearchView);
                        }
                    }

                }, 1000);

                return false;
            }
        });
    }

    private boolean isWorkmatesBottomNavigationSelected() {
        return binding.bottomNavView.getSelectedItemId() == R.id.navigation_workmates;
    }

    private void getWorkmatesByQueryOrDefault(boolean isSearchBoxEmpty) {

        if (isSearchBoxEmpty) {

            workMateViewModel.getWorkMates();

        } else {

            workMateViewModel.getWorkmatesByQuery(querySearchView);
        }
    }

}