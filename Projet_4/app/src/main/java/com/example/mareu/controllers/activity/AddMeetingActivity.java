package com.example.mareu.controllers.activity;


import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mareu.DI.DI;
import com.example.mareu.R;
import com.example.mareu.controllers.fragment.AddMeetingFragBtn;
import com.example.mareu.controllers.fragment.AddMeetingFragTxtInput;
import com.example.mareu.databinding.ActivityAddMeetingBinding;
import com.example.mareu.model.Meeting;
import com.example.mareu.service.MeetingApiService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class AddMeetingActivity extends AppCompatActivity {

    private ActivityAddMeetingBinding binding;
    private AddMeetingFragTxtInput addFragTxtInput;
    private AddMeetingFragBtn addFragBtn;
    private MeetingApiService meetingApiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUI();

        this.configureToolbar();
        this.configAddMeetingFragTxtInputAndShow();
        this.configAddMeetingFragBtnAndShow();
        meetingApiService = DI.getMeetingApiService();

    }

    private void initUI() {
        binding = ActivityAddMeetingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }

    private void configureToolbar() {
        setSupportActionBar(binding.includeToolbar.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    private void configAddMeetingFragTxtInputAndShow() {

        addFragTxtInput = (AddMeetingFragTxtInput) getSupportFragmentManager().findFragmentById(R.id.frame_layout_add_meeting_txt_input);

        if (addFragTxtInput == null) {
            addFragTxtInput = new AddMeetingFragTxtInput();

            getSupportFragmentManager().beginTransaction().add(R.id.frame_layout_add_meeting_txt_input, addFragTxtInput).commit();
        }
    }


    private void configAddMeetingFragBtnAndShow() {

        addFragBtn = (AddMeetingFragBtn) getSupportFragmentManager().findFragmentById(R.id.frame_layout_add_meeting_btn);

        if (addFragBtn == null) {
            addFragBtn = new AddMeetingFragBtn();

            getSupportFragmentManager().beginTransaction().add(R.id.frame_layout_add_meeting_btn, addFragBtn).commit();

            addFragBtn.runnableFragBtn = () -> {
                Meeting meeting = getDataMeeting();
                createMeeting(meeting);
            };
        }
    }

    private Meeting getDataMeeting() {

        int color = Objects.requireNonNull(addFragTxtInput.binding.colorPickerButton.getBackgroundTintList()).getDefaultColor();

        String subject = Objects.requireNonNull(addFragTxtInput.binding.textFieldSubject.getEditText()).getText().toString();

        String room = addFragBtn.binding.spinnerRoom.getSelectedItem().toString();

        String[] emailArrayStr = Objects.requireNonNull(addFragTxtInput.binding.textFieldListEmails.getEditText()).getText().toString().split("\n");

        ArrayList<String> emails = new ArrayList<>(Arrays.asList(emailArrayStr));

        Date hour = getHour();
        return new Meeting(color, subject, hour, room, emails);
    }

    private Date getHour() {
        Date date = new Date();
        int hour = addFragBtn.materialTimePicker.getHour();
        int minutes = addFragBtn.materialTimePicker.getMinute();
        long time = convertTimeToMillis(hour, minutes);
        date.setTime(time);
        return date;
    }

    public static long convertTimeToMillis(int hour, int minute) {
        return (((hour * 60L) + minute) * 60000);
    }

    private void createMeeting(Meeting meeting) {

        meetingApiService.createMeeting(meeting);
        MainActivity.resetFilter();

        Toast.makeText(getApplicationContext(), meeting.getSubject() + " Ajouté !", Toast.LENGTH_SHORT).show();
        finish();
    }
}
