package com.example.mareu.controllers.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.mareu.DI.DI;
import com.example.mareu.R;
import com.example.mareu.databinding.FragmentAddMeetingBinding;
import com.example.mareu.model.Reunion;
import com.example.mareu.service.DummyReunionApiService;
import com.example.mareu.service.ReunionApiService;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class AddMeetingFragment extends Fragment {

    private FragmentAddMeetingBinding mBinding;
    private MaterialTimePicker mMaterialTimePicker;
    private ReunionApiService mReunionApiService = DI.getReunionApiService();


    public AddMeetingFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentAddMeetingBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mBinding.btnTimePicker.setOnClickListener(v ->
                showTimePicker());
        mBinding.btnAdd.setOnClickListener(v ->
                addMeeting());
        this.configureToolbar();
    }

    private void configureToolbar() {
        ((AppCompatActivity) (requireActivity())).setSupportActionBar(mBinding.includeToolbar.toolbar);
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    private void showTimePicker() {
        int timeMode = getHeightDevice() < 720 ? MaterialTimePicker.INPUT_MODE_KEYBOARD : MaterialTimePicker.INPUT_MODE_CLOCK;
        MaterialTimePicker picker = new
                MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setInputMode(timeMode)
                .setTitleText(R.string.hour_meeting)
                .setHour(12)
                .setMinute(0)
                .build();


        picker.show(getParentFragmentManager(), "TAG");
        manageStateTimePicker(picker);
    }

    private  int getHeightDevice(){
        //GET DIMENSION DEVICE
        DisplayMetrics metrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }

    private void manageStateTimePicker(MaterialTimePicker picker) {
        picker.addOnPositiveButtonClickListener(v -> {

                    StringBuilder hourMeeting = new StringBuilder();
                    hourMeeting.append(picker.getHour())
                            .append(":")
                            .append(picker.getMinute());
                    Log.i("click", "addOnPositiveButtonClickListener");
                    mBinding.btnTimePicker.setText(hourMeeting);
                    mMaterialTimePicker = picker;
                }
        );

        picker.addOnNegativeButtonClickListener(v -> Log.i("click", "addOnNegativeButtonClickListener"));
        picker.addOnCancelListener(dialog -> Log.i("click", "addOnCancelListener"));
        picker.addOnDismissListener(dialog -> Log.i("click", "addOnDismissListener"));
    }

    private void addMeeting() {
        int color = Objects.requireNonNull(mBinding.colorPickerButton.getBackgroundTintList()).getDefaultColor();
        String subject = mBinding.textFieldSubject.getEditText().getText().toString();
        String room = mBinding.spinnerRoom.getSelectedItem().toString();

        Log.d("TAG", String.valueOf(color));
        Log.d("TAG", subject);
        Log.d("TAG", room);
        Log.d("TAG", String.valueOf(mMaterialTimePicker.getHour()));
        Date date = new Date();
        ArrayList<String> emails = new ArrayList<>();
        emails.add(mBinding.textFieldListEmails.getEditText().getText().toString());
        //Log.d("TAG", emails.get(1));

        Reunion reunion = new Reunion(subject, date, room, emails);
        Log.d("TAG", reunion.toString());
        mReunionApiService.createReunion(reunion);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}

