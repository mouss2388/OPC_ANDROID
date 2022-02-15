package com.example.mareu.controllers.activity;

import static android.widget.Spinner.MODE_DIALOG;
import static com.example.mareu.controllers.activity.AddMeetingActivity.convertTimeToMillis;
import static com.example.mareu.utlis.CustomTimePicker.getTimePicker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mareu.DI.DI;
import com.example.mareu.R;
import com.example.mareu.databinding.ActivityMainBinding;
import com.example.mareu.model.Meeting;
import com.example.mareu.service.MeetingApiService;
import com.google.android.material.timepicker.MaterialTimePicker;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static Context context;

    private ActivityMainBinding binding;
    private static final MeetingApiService meetingApiService = DI.getMeetingApiService();
    private static ArrayList<Meeting> meetings;
    public static MeetingAdapter meetingAdapter;

    public MaterialTimePicker materialTimePicker;

    private static boolean isSpinnerTouched = false;
    private static Spinner spinRooms;

    public static String filterApply;
    private static String roomFilter;
    private static long hourlyFilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        MainActivity.context = getApplicationContext();
        initUI();
        binding.btnAddReu.setOnClickListener(v -> {
            launchAddMeetingActivity();
        });
    }

    private void initUI() {

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        this.initData();
        this.initSpinnerDialog();
        this.configToolbar();
        this.initRecyclerView();
    }

    private void initData() {
        meetings = new ArrayList<>(meetingApiService.resetMeetings());
        filterApply = "reset";
    }

    private void initSpinnerDialog() {

        spinRooms = findViewById(R.id.mSpinner);
        spinRooms.setLayoutMode(MODE_DIALOG);
        String[] spinnerArray = getResources().getStringArray(R.array.room_array);


        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        spinRooms.setAdapter(spinnerAdapter);
        spinRooms.setOnItemSelectedListener(this);
    }

    private void configToolbar() {

        setSupportActionBar(binding.includeToolbar.toolbar);
    }

    private void initRecyclerView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(layoutManager);

        meetingAdapter = new MeetingAdapter(meetings);
        binding.recyclerView.setAdapter(meetingAdapter);
    }


    public void launchAddMeetingActivity() {
        startActivity(new Intent(MainActivity.this, AddMeetingActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.filter_hour:
                filterByHour();
                return true;

            case R.id.filter_room:
                filterByRoom();
                return true;

            case R.id.filter_reset:
                resetFilter();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void filterByHour() {
        materialTimePicker = getTimePicker(this);
        materialTimePicker.show(getSupportFragmentManager(), "TAG");
        manageStateTimePicker(materialTimePicker);
    }

    private void filterByRoom() {
        isSpinnerTouched = true;
        spinRooms.performClick();
    }

    public static void updateRecyclerView() {
        meetings.clear();
        switch (filterApply) {
            case "room":
                meetings.addAll(meetingApiService.filterMeetingByRoom(roomFilter));
                break;
            case "hour":
                meetings.addAll(meetingApiService.filterMeetingByHour(hourlyFilter));
                break;
            default:
                meetings.addAll(meetingApiService.getMeetings());
                break;
        }
        messageForListEmpty();
        meetingAdapter.notifyDataSetChanged();
    }

    private void manageStateTimePicker(MaterialTimePicker materialTimePicker) {
        materialTimePicker.addOnPositiveButtonClickListener(v -> {
                    int hour = materialTimePicker.getHour();
                    int minute = materialTimePicker.getMinute();
                    hourlyFilter = convertTimeToMillis(hour, minute);
                    filterApply = "hour";
                    updateRecyclerView();
                }
        );
        materialTimePicker.addOnCancelListener(v -> resetFilter());

        materialTimePicker.addOnNegativeButtonClickListener(v -> resetFilter());
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (isSpinnerTouched) {

            if (position > 0) {
                roomFilter = spinRooms.getSelectedItem().toString();
                filterApply = "room";
                updateRecyclerView();
            } else {
                Toast.makeText(getApplicationContext(), R.string.select_a_room, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private static void messageForListEmpty() {
        if (meetings.size() == 0)
            Toast.makeText(getAppContext(), R.string.meeting_not_founded, Toast.LENGTH_LONG).show();
    }

    private static Context getAppContext() {
        return MainActivity.context;
    }

    public static void resetFilter() {
        isSpinnerTouched = false;
        filterApply = "reset";
        spinRooms.setSelection(0);
        Toast.makeText(context, "Filtre réinitialisé", Toast.LENGTH_SHORT).show();
        updateRecyclerView();
    }
}
