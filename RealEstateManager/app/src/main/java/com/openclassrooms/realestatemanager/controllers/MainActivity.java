package com.openclassrooms.realestatemanager.controllers;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import com.google.android.material.navigation.NavigationView;
import com.openclassrooms.realestatemanager.R;
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




}
