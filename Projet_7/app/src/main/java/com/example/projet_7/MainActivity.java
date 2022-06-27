package com.example.projet_7;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.projet_7.databinding.ActivityMainBinding;

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

        binding.bntLoginFacebook.setOnClickListener(v -> Toast.makeText(this, getResources().getString(R.string.snackbar_msg_login_success) + " Facebook", Toast.LENGTH_SHORT).show());

        binding.bntLoginGoogle.setOnClickListener(v -> Toast.makeText(this, getResources().getString(R.string.snackbar_msg_login_success) + " Google", Toast.LENGTH_SHORT).show());

        binding.bntLoginEmail.setOnClickListener(v -> Toast.makeText(this, getResources().getString(R.string.snackbar_msg_login_success) + " Email", Toast.LENGTH_SHORT).show());

    }
}