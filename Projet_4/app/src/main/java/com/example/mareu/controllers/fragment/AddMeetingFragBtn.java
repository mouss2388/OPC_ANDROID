package com.example.mareu.controllers.fragment;

import static com.example.mareu.controllers.fragment.AddMeetingFragTxtInput.isEmailsValid;
import static com.example.mareu.controllers.fragment.AddMeetingFragTxtInput.isSubjectValid;
import static com.example.mareu.utlis.CustomTimePicker.MEETING_DURATION;
import static com.example.mareu.utlis.CustomTimePicker.convertDurationMeetingInMilli;
import static com.example.mareu.utlis.CustomTimePicker.getHourSelectedInMilli;
import static com.example.mareu.utlis.CustomTimePicker.getTimePicker;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import java.util.ArrayList;


public class AddMeetingFragBtn extends Fragment implements View.OnTouchListener {


    public FragmentAddMeetingButtonBinding binding;
    public MaterialTimePicker materialTimePicker;
    private boolean spinnerRoomTouched, roomIsAvailable, roomSelected;

    private MeetingApiService meetingApiService;

    public Runnable runnableFragBtn;
    private Handler handler;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        meetingApiService = DI.getMeetingApiService();
    }

    private void initUI() {
        handler = new Handler(Looper.getMainLooper());
        handler.post(checkInputText);

        materialTimePicker = null;
        spinnerRoomTouched = false;
        roomSelected = false;
        roomIsAvailable = false;
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

    private void checkFormIsValid() {

        boolean btnIsValid = isSubjectValid && isEmailsValid && materialTimePicker != null && roomSelected && roomIsAvailable;
        binding.btnAdd.setEnabled(btnIsValid);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAddMeetingButtonBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        initOnClickListener();
    }


    private void initOnClickListener() {

        binding.btnTimePicker.setOnClickListener(v -> {

            materialTimePicker = getTimePicker(getActivity());
            materialTimePicker.show(getParentFragmentManager(), "TAG_PICKER");
            manageStateTimePicker();
        });

        binding.btnAdd.setOnClickListener(v ->
                onSubmit());

        changeRoom();
        binding.spinnerRoom.setOnTouchListener(this);
    }


    private void manageStateTimePicker() {

        materialTimePicker.addOnPositiveButtonClickListener(v -> {

                    StringBuilder hourMeeting = new StringBuilder();
                    hourMeeting.append(materialTimePicker.getHour() < 10 ? "0" + materialTimePicker.getHour() : materialTimePicker.getHour())
                            .append(":")
                            .append(materialTimePicker.getMinute() < 10 ? "0" + materialTimePicker.getMinute() : materialTimePicker.getMinute());
                    binding.btnTimePicker.setText(hourMeeting);
                    checkRoomIsAvailable();
                }
        );
        materialTimePicker.addOnCancelListener(v -> {
            materialTimePicker = null;
            binding.btnTimePicker.setText(R.string.set_time);
        });
        materialTimePicker.addOnNegativeButtonClickListener(v -> {
            materialTimePicker = null;
            binding.btnTimePicker.setText(R.string.set_time);
        });
    }


    private void checkRoomIsAvailable() {

        long meetingHourlyInMilli = getHourSelectedInMilli(materialTimePicker);
        long meetingDuration = convertDurationMeetingInMilli();
        String room = getRoomNotAvailable(meetingHourlyInMilli, meetingDuration);

        roomIsAvailable = room.equals("");

        if (!roomIsAvailable) {
            Toast.makeText(getContext(), "Salle " + room + " non disponible pour cet horaire, une réunion dure environ " + MEETING_DURATION + " min \n veuillez changer de salle ou d'horaire", Toast.LENGTH_LONG).show();
        }
    }

    private String getRoomNotAvailable(long meeting_hourly_reservation, long time_between_meeting) {

        ArrayList<Meeting> meetings = new ArrayList<>(meetingApiService.getMeetings());
        String room = "";
        String room_selected = binding.spinnerRoom.getSelectedItem().toString();

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

    private void onSubmit() {
        runnableFragBtn.run();
    }

    private void changeRoom() {
        binding.spinnerRoom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinnerRoomTouched) {
                    //TODO Simplify Block If just test roomSelected
                    if (position > 0) {
                        roomSelected = true;
                        if (materialTimePicker != null) {
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        handler.removeCallbacks(checkInputText);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        spinnerRoomTouched = true;
        binding.spinnerRoom.performClick();
        return false;
    }
}