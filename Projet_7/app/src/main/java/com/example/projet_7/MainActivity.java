package com.example.projet_7;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projet_7.databinding.ActivityMainBinding;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.snackbar.Snackbar;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ActivityResultLauncher<Intent> startForResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_SignInActivity);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        this.handleResponseAfterSignIn();
        this.setupListeners();
    }

    private void handleResponseAfterSignIn() {
        startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

            if (result.getResultCode() == RESULT_OK) {
//                userManager.createUser();
                showSnackBar(getResources().getString(R.string.snackbar_msg_login_success));

            } else if (result.getResultCode() == RESULT_CANCELED) {
                showSnackBar(getResources().getString(R.string.snackbar_msg_login_cancelled));
            } else {
                showSnackBar(getResources().getString(R.string.snackbar_msg_error_unknow));
            }
        });
    }

    // Show Snack Bar with a message
    private void showSnackBar(String message) {
        Snackbar.make(binding.mainLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    private void setupListeners() {

        binding.bntLoginFacebook.setOnClickListener(v ->
                showSnackBar(getResources().getString(R.string.snackbar_msg_login_success) + " Facebook"));


        binding.bntLoginGoogle.setOnClickListener(v ->
                showSnackBar(getResources().getString(R.string.snackbar_msg_login_success) + " Google"));

        binding.bntLoginEmail.setOnClickListener(v -> signInWith("email"));

    }


    private void signInWith(String provider) {
        //AuthUI provider
        AuthUI.IdpConfig authProvider;

        // Choose authentication providers
        switch (provider) {
            case "facebook":
                authProvider = new AuthUI.IdpConfig.FacebookBuilder().build();
                break;
            case "google":
                authProvider = new AuthUI.IdpConfig.GoogleBuilder().build();
                break;
            default:
                authProvider = new AuthUI.IdpConfig.EmailBuilder().build();
                break;
        }

        Intent intentSignIn = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(Collections.singletonList(authProvider))
                .setIsSmartLockEnabled(false, true)
                .build();
        startForResult.launch(intentSignIn);
    }
}