package com.example.mareu.controllers.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mareu.R;
import com.example.mareu.controllers.fragment.AddMeetingFragment;
import com.example.mareu.databinding.ActivityAddMeetingBinding;

public class AddMeetingActivity extends AppCompatActivity {

    private ActivityAddMeetingBinding mBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        this.configAndShowAddMeetingFragment();
    }

    private void initUI() {
        mBinding = ActivityAddMeetingBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);
    }


    private void configAndShowAddMeetingFragment() {
        AddMeetingFragment addMeetingFragment = (AddMeetingFragment) getSupportFragmentManager().findFragmentById(R.id.frame_layout_add_meeting);

        if (addMeetingFragment == null) {
            addMeetingFragment = new AddMeetingFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.frame_layout_add_meeting, addMeetingFragment).commit();
        }
    }
}