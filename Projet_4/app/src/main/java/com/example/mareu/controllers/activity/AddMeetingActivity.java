package com.example.mareu.controllers.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mareu.R;
import com.example.mareu.controllers.fragment.AddMeetingButtonFragment;
import com.example.mareu.controllers.fragment.AddMeetingFragment;
import com.example.mareu.databinding.ActivityAddMeetingBinding;

public class AddMeetingActivity extends AppCompatActivity{

    private ActivityAddMeetingBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();

        this.configureToolbar();
        this.configAddMeetingFragAndShowFragment();
        if (isTablet()) {
            this.configAddMeetingBtnFragAndShowFragment();
        }
    }

    private void initUI() {
        mBinding = ActivityAddMeetingBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);
    }

    private void configureToolbar() {
        setSupportActionBar(mBinding.includeToolbar.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    private void configAddMeetingFragAndShowFragment() {

        AddMeetingFragment addFragment = (AddMeetingFragment) getSupportFragmentManager().findFragmentById(R.id.frame_layout_add_meeting);

        if (addFragment == null &&
                findViewById(R.id.frame_layout_add_meeting) != null) {
            addFragment = new AddMeetingFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.frame_layout_add_meeting, addFragment).commit();
        }
    }

    private void configAddMeetingBtnFragAndShowFragment() {
        AddMeetingButtonFragment addFragment = (AddMeetingButtonFragment) getSupportFragmentManager().findFragmentById(R.id.frame_layout_add_meeting_button);

        if (addFragment == null &&
                findViewById(R.id.frame_layout_add_meeting_button) != null) {
            addFragment = new AddMeetingButtonFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.frame_layout_add_meeting_button, addFragment).commit();
        }
    }

    private boolean isTablet() {
        return getResources().getBoolean(R.bool.isTablet);
    }

}