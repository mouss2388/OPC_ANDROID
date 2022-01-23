package com.example.mareu.controllers.activity;

import static com.example.mareu.controllers.activity.MainActivity.updateRecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mareu.DI.DI;
import com.example.mareu.R;
import com.example.mareu.controllers.fragment.AddMeetingButtonFragment;
import com.example.mareu.controllers.fragment.AddMeetingFragment;
import com.example.mareu.databinding.ActivityAddMeetingBinding;
import com.example.mareu.model.Meeting;
import com.example.mareu.service.MeetingApiService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class AddMeetingActivity extends AppCompatActivity {

    private ActivityAddMeetingBinding mBinding;
    private AddMeetingFragment addFragment;
    private AddMeetingButtonFragment addFragmentBtn;
    private MeetingApiService mMeetingApiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUI();

        this.configureToolbar();
        this.configAddMeetingFragAndShowFragment();
        this.configAddMeetingBtnFragAndShowFragment();
        mMeetingApiService = DI.getReunionApiService();

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

        addFragment = (AddMeetingFragment) getSupportFragmentManager().findFragmentById(R.id.frame_layout_add_meeting);

        if (addFragment == null &&
                findViewById(R.id.frame_layout_add_meeting) != null) {
            addFragment = new AddMeetingFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.frame_layout_add_meeting, addFragment).commit();

        }
    }


    private void configAddMeetingBtnFragAndShowFragment() {
        addFragmentBtn = (AddMeetingButtonFragment) getSupportFragmentManager().findFragmentById(R.id.frame_layout_add_meeting_btn);

        if (addFragmentBtn == null &&
                findViewById(R.id.frame_layout_add_meeting_btn) != null) {

            addFragmentBtn = new AddMeetingButtonFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.frame_layout_add_meeting_btn, addFragmentBtn).commit();

            addFragmentBtn.runnableFragBtn = () -> {
                Log.i("test", "OnSubmit Click");
                Log.i("test", String.valueOf(addFragmentBtn.mMaterialTimePicker.getHour()));

                Meeting meeting = getDataMeeting();
                createMeeting(meeting);
            };
        }
    }

    private Meeting getDataMeeting() {
        int color = Objects.requireNonNull(addFragment.mBinding.colorPickerButton.getBackgroundTintList()).getDefaultColor();

        String subject = Objects.requireNonNull(addFragment.mBinding.textFieldSubject.getEditText()).getText().toString();

        String room = addFragmentBtn.mBinding.spinnerRoom.getSelectedItem().toString();
        String[] emailArrayStr = Objects.requireNonNull(addFragment.mBinding.textFieldListEmails.getEditText()).getText().toString().split("\n");

        ArrayList<String> emails = new ArrayList<>(Arrays.asList(emailArrayStr));


        Date hourly = getHourly();
        return new Meeting(color, subject, hourly, room, emails);
    }

    private void createMeeting(Meeting meeting) {
        mMeetingApiService.createReunion(meeting);
        updateRecyclerView();
        Toast.makeText(getApplicationContext(), "Réunion ajoutée!", Toast.LENGTH_SHORT).show();
        finish();
    }


    private long convertTimeToMillis(int hour, int minute) {
        return (((hour * 60L) + minute) * 60000);
    }

    public static String convertMillisToStr(long millis) {
        int heure = (int) (millis / 60000) / 60;
        int minute = (int) (millis / 60000) % 60;
        StringBuilder horaire = new StringBuilder()
                .append(heure)
                .append(":");
        if (minute == 0)
            horaire.append("00");
        else
            horaire.append(minute);
        return String.valueOf(horaire);
    }

    private Date getHourly() {
        Date date = new Date();
        int hour = addFragmentBtn.mMaterialTimePicker.getHour();
        int minutes = addFragmentBtn.mMaterialTimePicker.getMinute();
        long time = convertTimeToMillis(hour, minutes);
        date.setTime(time);
        return date;
    }
}