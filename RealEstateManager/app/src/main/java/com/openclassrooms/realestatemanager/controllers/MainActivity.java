package com.openclassrooms.realestatemanager.controllers;

import static com.openclassrooms.realestatemanager.utils.Utils.USER_LOGGED_FORMAT_JSON;
import static com.openclassrooms.realestatemanager.utils.Utils.concatStr;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.database.model.User;
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;
import com.openclassrooms.realestatemanager.utils.Utils;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    String TAG = MainActivity.this.getClass().getSimpleName();

    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        this.configureTextViewMain();
        this.configureTextViewQuantity();
        this.configureMenu();
    }

    private void configureTextViewMain() {
//        binding.activityMainActivityTextViewMain.setTextSize(Float.parseFloat("15"));
//        binding.activityMainActivityTextViewMain.setText("Le premier bien immobilier enregistré vaut ");
    }

    private void configureTextViewQuantity() {
        int quantity = Utils.convertDollarToEuro(100);
//        binding.activityMainActivityTextViewQuantity.setTextSize(Float.parseFloat("20"));
//        binding.activityMainActivityTextViewQuantity.setText(String.valueOf(quantity));
    }

    private void configureMenu() {
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();
        this.updateMenuWithUserData();
    }

    private void configureToolBar() {
        binding.activityMainToolbar.setTitle(R.string.app_name);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_Item_1) {
            Toast.makeText(this, "Click On Item 1", Toast.LENGTH_SHORT).show();


        } else if (id == R.id.menu_Item_2) {
            Toast.makeText(this, "Click On Item 2", Toast.LENGTH_SHORT).show();


        } else if (id == R.id.menu_Item_3) {
            Toast.makeText(this, "Click On Item 3", Toast.LENGTH_SHORT).show();

        }

        binding.mainLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    private User getUserJsonInUserObject() {

        Gson gson = new Gson();
        return gson.fromJson(getIntent().getStringExtra(USER_LOGGED_FORMAT_JSON), User.class);
    }


    private void updateMenuWithUserData() {

        User user = getUserJsonInUserObject();

        TextView userName = accessMenuHeaderInfo().findViewById(R.id.user_Name);
        userName.setText(concatStr(user.getFirstname(), user.getLastname()));

        TextView userEmail = accessMenuHeaderInfo().findViewById(R.id.user_Email);
        userEmail.setText(user.getEmail());

        if (user.getPicture() != null) {
            setProfilePicture(user);
        }

    }


    private void setProfilePicture(User user) {
        Glide.with(this)
                .load(user.getPicture())
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .apply(RequestOptions.circleCropTransform())
                .into((ImageView) accessMenuHeaderInfo().findViewById(R.id.user_Picture));
    }

    private View accessMenuHeaderInfo() {
        return binding.activityMainNavView.getHeaderView(0);
    }
}
