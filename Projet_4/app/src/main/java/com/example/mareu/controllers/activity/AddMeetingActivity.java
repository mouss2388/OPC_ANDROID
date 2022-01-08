package com.example.mareu.controllers.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mareu.databinding.ActivityAddMeetingBinding;

public class AddMeetingActivity extends AppCompatActivity {

    private ActivityAddMeetingBinding mBinding;
    private final String TAG = AddMeetingActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityAddMeetingBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);
        this.configureToolbar();

    }

    private void configureToolbar() {
        setSupportActionBar(mBinding.includeToolbar.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}