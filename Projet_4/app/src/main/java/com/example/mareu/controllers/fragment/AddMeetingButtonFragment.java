package com.example.mareu.controllers.fragment;

import static com.example.mareu.controllers.activity.AddMeetingActivity.convertTimeToMillis;
import static com.example.mareu.controllers.fragment.AddMeetingFragment.isEmailsValid;
import static com.example.mareu.controllers.fragment.AddMeetingFragment.isSubjectValid;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mareu.DI.DI;
import com.example.mareu.R;
import com.example.mareu.databinding.FragmentAddMeetingButtonBinding;
import com.example.mareu.model.Meeting;
import com.example.mareu.service.MeetingApiService;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.ArrayList;


public class AddMeetingButtonFragment extends Fragment implements View.OnTouchListener {

    public FragmentAddMeetingButtonBinding mBinding;
    public MaterialTimePicker mMaterialTimePicker;
    private boolean roomIsAvailable, spinnerRoomTouched, roomSelected;
    private MeetingApiService mMeetingApiService;
    public Runnable runnableFragBtn;
    private Handler handler;
    private int MEETING_DURATION;


    public AddMeetingButtonFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        mMeetingApiService = DI.getReunionApiService();
    }

    private void initUI() {
        handler = new Handler(Looper.getMainLooper());
        handler.post(checkInputText);
        mMaterialTimePicker = null;
        MEETING_DURATION = 45;
        roomSelected = false;
        roomIsAvailable = false;
        spinnerRoomTouched = false;
    }

    private void checkFormIsValid() {
        boolean btnIsValid = isSubjectValid && isEmailsValid && mMaterialTimePicker != null && roomSelected && roomIsAvailable;
        mBinding.btnAdd.setEnabled(btnIsValid);
    }

    private final Runnable checkInputText = new Runnable() {
        public void run() {
            try {
                checkFormIsValid();
                handler.postDelayed(this, 100);
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
        changeRoom();
        mBinding.spinnerRoom.setOnTouchListener(this);
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
                .setHour(0)
                .setMinute(0)
                .build();

        mMaterialTimePicker.show(getParentFragmentManager(), "TAG_PICKER");
        manageStateTimePicker(mMaterialTimePicker);
    }


    private void manageStateTimePicker(MaterialTimePicker materialTimePicker) {
        materialTimePicker.addOnPositiveButtonClickListener(v -> {

                    StringBuilder hourMeeting = new StringBuilder();
                    hourMeeting.append(materialTimePicker.getHour() < 10 ? "0" + materialTimePicker.getHour() : materialTimePicker.getHour())
                            .append(":")
                            .append(materialTimePicker.getMinute() < 10 ? "0" + materialTimePicker.getMinute() : materialTimePicker.getMinute());
                    mBinding.btnTimePicker.setText(hourMeeting);
                    checkRoomIsAvailable();
                }
        );
        materialTimePicker.addOnCancelListener(v -> {
            mMaterialTimePicker = null;
            mBinding.btnTimePicker.setText(R.string.set_time);
        });
        materialTimePicker.addOnNegativeButtonClickListener(v -> {
            mMaterialTimePicker = null;
            mBinding.btnTimePicker.setText(R.string.set_time);
        });
    }


    private long getHourlySelectedInMilli() {
        int hour = mMaterialTimePicker.getHour();
        int minute = mMaterialTimePicker.getMinute();
        return convertTimeToMillis(hour, minute);
    }

    private long convertDurationMeetingInMilli() {
        return convertTimeToMillis(0, MEETING_DURATION);
    }

    private String getRoomNotAvailable(long meeting_hourly_reservation, long time_between_meeting) {

        ArrayList<Meeting> meetings = new ArrayList<>(mMeetingApiService.getReunions());
        String room = "";
        String room_selected = mBinding.spinnerRoom.getSelectedItem().toString();

        for (int pos = 0; pos < meetings.size(); pos++) {
            if (room_selected.equalsIgnoreCase(meetings.get(pos).getRoom())) {
                long meeting_hourly = meetings.get(pos).getHour().getTime();
                long diff = Math.abs(meeting_hourly - meeting_hourly_reservation);
                long diff1 = (3600 * 24 * 1000) - diff;

                if (diff < time_between_meeting || diff1 < time_between_meeting) {
                    room = meetings.get(pos).getRoom();
                    break;
                }
            }
        }
        return room;
    }

    private void checkRoomIsAvailable() {
        long meetingHourlyInMilli = getHourlySelectedInMilli();
        long meetingDuration = convertDurationMeetingInMilli();
        String room = getRoomNotAvailable(meetingHourlyInMilli, meetingDuration);

        roomIsAvailable = room.equals("");

        if (!roomIsAvailable) {
            Toast.makeText(getContext(), "Salle " + room + " non disponible pour cet horaire, une réunion dure environ " + MEETING_DURATION + " min \n veuillez changer de salle ou d'horaire", Toast.LENGTH_LONG).show();
        }
    }


    private void changeRoom() {
        mBinding.spinnerRoom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinnerRoomTouched) {
                    if (position > 0) {
                        roomSelected = true;
                        if (mMaterialTimePicker != null) {
                            checkRoomIsAvailable();
                        }
                    } else {
                        roomSelected = false;
                        Toast.makeText(getContext(), "Selectionnez une salle", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        spinnerRoomTouched = true;
        mBinding.spinnerRoom.performClick();
        return false;
    }
}