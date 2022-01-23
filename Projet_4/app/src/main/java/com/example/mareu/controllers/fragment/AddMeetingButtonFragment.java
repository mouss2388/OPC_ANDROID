package com.example.mareu.controllers.fragment;

import static com.example.mareu.controllers.fragment.AddMeetingFragment.isEmailsValid;
import static com.example.mareu.controllers.fragment.AddMeetingFragment.isSubjectValid;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mareu.R;
import com.example.mareu.databinding.FragmentAddMeetingButtonBinding;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;


public class AddMeetingButtonFragment extends Fragment {

    public FragmentAddMeetingButtonBinding mBinding;
    public MaterialTimePicker mMaterialTimePicker;
    public Runnable runnableFragBtn;
    private Handler handler;


    public AddMeetingButtonFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();

    }

    private void initUI() {
        handler = new Handler(Looper.getMainLooper());
        handler.post(checkInputText);
        mMaterialTimePicker = null;
    }

    private void checkFormIsValid() {
        Log.i("Rooms", mBinding.spinnerRoom.getSelectedItem().toString());
        mBinding.btnAdd.setEnabled(mMaterialTimePicker != null && isSubjectValid && isEmailsValid);
    }

    private final Runnable checkInputText = new Runnable() {
        public void run() {
            try {
                checkFormIsValid();
                handler.postDelayed(this, 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentAddMeetingButtonBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initOnClickListener();
    }


    private void initOnClickListener() {
        mBinding.btnTimePicker.setOnClickListener(v ->
                showTimePicker());
        mBinding.btnAdd.setOnClickListener(v ->
                onSubmit());
    }


    private int getHeightDevice() {
        //GET DIMENSION DEVICE
        DisplayMetrics metrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }

    private void showTimePicker() {
        int timeMode = getHeightDevice() < 720 ? MaterialTimePicker.INPUT_MODE_KEYBOARD : MaterialTimePicker.INPUT_MODE_CLOCK;
        mMaterialTimePicker = new
                MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setInputMode(timeMode)
                .setTitleText(R.string.hour_meeting)
                .setHour(12)
                .setMinute(0)
                .build();

        mMaterialTimePicker.show(getParentFragmentManager(), "TAG_PICKER");
        manageStateTimePicker(mMaterialTimePicker);
    }


    private void manageStateTimePicker(MaterialTimePicker materialTimePicker) {
        materialTimePicker.addOnPositiveButtonClickListener(v -> {

                    StringBuilder hourMeeting = new StringBuilder();
                    hourMeeting.append(materialTimePicker.getHour() == 0 ? "00" : materialTimePicker.getHour())
                            .append(":")
                            .append(materialTimePicker.getMinute() == 0 ? "00" : materialTimePicker.getMinute());
                    Log.i("click", "addOnPositiveButtonClickListener");
                    mBinding.btnTimePicker.setText(hourMeeting);
                }
        );
        materialTimePicker.addOnCancelListener(v -> mMaterialTimePicker = null);
        materialTimePicker.addOnNegativeButtonClickListener(v -> mMaterialTimePicker = null);
    }


    private void onSubmit() {
        runnableFragBtn.run();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
        handler.removeCallbacks(checkInputText);
    }
}