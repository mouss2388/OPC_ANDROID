package com.example.projet_7.ui;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projet_7.R;
import com.example.projet_7.databinding.ActivityMainBinding;
import com.example.projet_7.utils.Utils;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Utils.showSnackBar(binding.mainLayout, getString(R.string.snackbar_msg_login_success));
    }

}