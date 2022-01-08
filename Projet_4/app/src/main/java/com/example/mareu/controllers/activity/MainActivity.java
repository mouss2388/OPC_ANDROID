package com.example.mareu.controllers.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.mareu.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        this.configureToolbar();
        mBinding.btnAddReu.setOnClickListener(v -> btn_add());
    }

    private void configureToolbar() {
        setSupportActionBar(mBinding.includeToolbar.toolbar);
    }


    public void btn_add() {
        Log.i(TAG, "click on btn_add");
    }
}