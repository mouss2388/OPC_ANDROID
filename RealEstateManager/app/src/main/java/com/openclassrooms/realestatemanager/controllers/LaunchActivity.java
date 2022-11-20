package com.openclassrooms.realestatemanager.controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.openclassrooms.realestatemanager.databinding.ActivityLaunchBinding;
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;
import com.openclassrooms.realestatemanager.utils.Utils;

public class LaunchActivity extends AppCompatActivity {

    private ActivityLaunchBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLaunchBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        this.setupListeners();
    }

    private void setupListeners() {

        binding.btnSignUp.setOnClickListener(v -> Utils.startSignActivity(this, Utils.SIGN_UP));
        binding.btnSignIn.setOnClickListener(v -> Utils.startSignActivity(this, Utils.SIGN_IN));
    }
}