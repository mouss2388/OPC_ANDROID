package com.example.projet_7.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.projet_7.R;
import com.example.projet_7.databinding.ActivityMainBinding;
import com.example.projet_7.ui.restaurants.RestaurantsFragment;
import com.example.projet_7.ui.maps.MapsFragment;
import com.example.projet_7.ui.workmates.WorkmatesFragment;
import com.example.projet_7.utils.Utils;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        // 6 - Configure all views

        this.configureToolBar();

        this.configureDrawerLayout();

        this.configureNavigationView();

        String urlPictureTest = "https://randomuser.me/api/portraits/women/75.jpg";
        this.setProfilePicture(urlPictureTest);

        this.showFirstFrag();
        this.setupListernerBottomNav();
    }

    private void showFirstFrag(){
        replaceFragment(new MapsFragment());
    }
    private void setupListernerBottomNav() {

        binding.bottomNavView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_maps:
                    replaceFragment(new MapsFragment());
                    break;
                case R.id.navigation_restaurants:
                    replaceFragment(new RestaurantsFragment());
                    break;
                case R.id.navigation_workmates:
                    replaceFragment(new WorkmatesFragment());
                    break;
            }
            return true;
        });
    }

    private  void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.activity_main_frame_layout,fragment);
        fragmentTransaction.commit();
    }

    private void setProfilePicture(String profilePictureUrl) {
        View navHeader = binding.activityMainNavView.getHeaderView(0);
        Glide.with(this)
                .load(profilePictureUrl)
                .apply(RequestOptions.circleCropTransform())
                .into((ImageView) navHeader.findViewById(R.id.user_Picture));
    }

    @Override
    protected void onStart() {
        super.onStart();
        Utils.showSnackBar(binding.mainLayout, getString(R.string.snackbar_msg_login_success));
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


    // 3 - Configure NavigationView
    private void configureNavigationView() {
        binding.activityMainNavView.setNavigationItemSelectedListener(this);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_Item_1) {
            Toast.makeText(this, "menu_Item_1", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.menu_Item_2) {
            Toast.makeText(this, "menu_Item_2", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.menu_Item_3) {
            Toast.makeText(this, "menu_Item_3", Toast.LENGTH_SHORT).show();

        }

        binding.mainLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}