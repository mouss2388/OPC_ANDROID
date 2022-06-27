package com.example.projet_7;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.projet_7.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_SignInActivity);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        this.setupListeners();
    }

    private void setupListeners() {

        binding.bntLoginFacebook.setOnClickListener(v ->
                showSnackBar(getResources().getString(R.string.snackbar_msg_login_success) + " Facebook"));


        binding.bntLoginGoogle.setOnClickListener(v ->
                showSnackBar(getResources().getString(R.string.snackbar_msg_login_success) + " Google"));

        binding.bntLoginEmail.setOnClickListener(v ->
                        showSnackBar(getResources().getString(R.string.snackbar_msg_login_success) + " Email"));
    }

    // Show Snack Bar with a message
    private void showSnackBar(String message) {
        Snackbar.make(binding.mainLayout, message, Snackbar.LENGTH_SHORT).show();
    }
}