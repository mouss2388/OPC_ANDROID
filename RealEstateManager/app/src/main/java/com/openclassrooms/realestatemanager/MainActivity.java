package com.openclassrooms.realestatemanager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

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
    }

    private void configureTextViewMain() {
        binding.activityMainActivityTextViewMain.setTextSize(Float.parseFloat("15"));
        binding.activityMainActivityTextViewMain.setText("Le premier bien immobilier enregistré vaut ");
    }

    private void configureTextViewQuantity() {
        int quantity = Utils.convertDollarToEuro(100);
        binding.activityMainActivityTextViewQuantity.setTextSize(Float.parseFloat("20"));
        binding.activityMainActivityTextViewQuantity.setText(String.valueOf(quantity));
    }
}
