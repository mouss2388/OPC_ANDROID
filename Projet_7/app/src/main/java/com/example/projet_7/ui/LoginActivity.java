package com.example.projet_7.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projet_7.R;
import com.example.projet_7.databinding.ActivityLoginBinding;
import com.example.projet_7.manager.UserManager;
import com.example.projet_7.utils.Utils;
import com.firebase.ui.auth.AuthUI;

import java.util.Collections;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private ActivityResultLauncher<Intent> startForResult = null;
    private final UserManager userManager = UserManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_SignInActivity);
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        this.handleResponseAfterSignIn();
        this.setupListeners();
    }

    private void handleResponseAfterSignIn() {
        startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

            if (result.getResultCode() == RESULT_OK) {
                userManager.isUserExists().addOnSuccessListener(documentSnapshot -> {
                    if (!documentSnapshot.exists()) {
                        userManager.createUser();
                    }
                    startMainActivity();
                });

            } else if (result.getResultCode() == RESULT_CANCELED) {
                Utils.showSnackBar(binding.loginLayout, getString(R.string.snackbar_msg_login_cancelled));
            } else {
                Utils.showSnackBar(binding.loginLayout, getString(R.string.snackbar_msg_error_unknow));
            }
        });
    }


    // Launching Profile Activity
    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void setupListeners() {

        binding.bntLoginFacebook.setOnClickListener(v -> signInWith("facebook"));

        binding.bntLoginGoogle.setOnClickListener(v -> signInWith("google"));

        binding.bntLoginEmail.setOnClickListener(v -> signInWith("email"));

    }


    private void signInWith(String provider) {
        //AuthUI provider
        AuthUI.IdpConfig authUiConfig;

        // Choose authentication providers
        switch (provider) {
            case "facebook":
                authUiConfig = new AuthUI.IdpConfig.FacebookBuilder().build();
                break;
            case "google":
                authUiConfig = new AuthUI.IdpConfig.GoogleBuilder().build();
                break;
            default:
                authUiConfig = new AuthUI.IdpConfig.EmailBuilder().build();
                break;
        }

        Intent intentLogin = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(Collections.singletonList(authUiConfig))
                .setIsSmartLockEnabled(false, true)
                .build();
        startForResult.launch(intentLogin);
    }
}