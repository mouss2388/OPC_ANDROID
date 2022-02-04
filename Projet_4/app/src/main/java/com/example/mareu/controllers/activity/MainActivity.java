package com.example.mareu.controllers.activity;

import static android.widget.Spinner.MODE_DIALOG;
import static com.example.mareu.controllers.activity.AddMeetingActivity.convertTimeToMillis;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
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
import com.google.android.material.timepicker.TimeFormat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private final String TAG = MainActivity.class.getSimpleName();
    private static Context context;


    private ActivityMainBinding mBinding;
    private static final MeetingApiService mMeetingApiService = DI.getReunionApiService();
    private static ArrayList<Meeting> mMeetings;
    public static MeetingAdapter meetingAdapter;
    public MaterialTimePicker mMaterialTimePicker;
    private boolean isSpinnerTouched = false;
    private Spinner spinner;
    public static String filterApply;
    private static String roomFilter;
    private static long hourlyFilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.context = getApplicationContext();

        initUI();
        this.configureToolbar();
        this.initRecyclerView();

        mBinding.btnAddReu.setOnClickListener(v -> {
            resetFilter();
            launchAddMeetingActivity();
        });
    }

    private void initUI() {
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);
        initData();
        initSpinnerDialog();
    }

    private void initSpinnerDialog() {

        spinner = findViewById(R.id.mSpinner);
        spinner.setLayoutMode(MODE_DIALOG);
        String[] spinnerArray = getResources().getStringArray(R.array.room_array);


        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);
    }


    private void initData() {
        mMeetings = new ArrayList<>(mMeetingApiService.resetReunions());
        filterApply = "reset";
    }

    private void configureToolbar() {
        setSupportActionBar(mBinding.includeToolbar.toolbar);
    }


    public void launchAddMeetingActivity() {
        startActivity(new Intent(MainActivity.this, AddMeetingActivity.class));
    }

    ////////////MENU/////////////

    /**
     * @param menu which replace by menu_activity_main
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.filter_hour:
                showTimePicker();
                return true;
            case R.id.filter_place:
                spinner.performClick();
                isSpinnerTouched = true;
                return true;
            case R.id.filter_reset:
                filterApply = "reset";
                updateRecyclerView();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mBinding.recyclerView.setLayoutManager(layoutManager);
        meetingAdapter = new MeetingAdapter(mMeetings);

        mBinding.recyclerView.setAdapter(meetingAdapter);
    }

    public static void updateRecyclerView() {
        mMeetings.clear();
        switch (filterApply) {
            case "room":
                mMeetings.addAll(mMeetingApiService.filterMeetingByRoom(roomFilter));
                break;
            case "hourly":
                mMeetings.addAll(mMeetingApiService.filterMeetingByHour(hourlyFilter));
                break;
            default:
                mMeetings.addAll(mMeetingApiService.getReunions());
                break;
        }
        messageForListEmpty();
        meetingAdapter.notifyDataSetChanged();
    }

    private int getHeightDevice() {
        //GET DIMENSION DEVICE
        DisplayMetrics metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
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

        mMaterialTimePicker.show(getSupportFragmentManager(), "TAG");
        manageStateTimePicker(mMaterialTimePicker);
    }

    private void manageStateTimePicker(MaterialTimePicker materialTimePicker) {
        materialTimePicker.addOnPositiveButtonClickListener(v -> {
                    int hour = mMaterialTimePicker.getHour();
                    int minute = materialTimePicker.getMinute();
                    hourlyFilter = convertTimeToMillis(hour, minute);
                    filterApply = "hourly";
                    updateRecyclerView();
                }
        );
        materialTimePicker.addOnCancelListener(v -> {
            filterApply = "reset";
            Toast.makeText(getApplicationContext(), "addOnCancelListener", Toast.LENGTH_SHORT).show();
            updateRecyclerView();
        });

        materialTimePicker.addOnNegativeButtonClickListener(v -> {
            filterApply = "reset";
            Toast.makeText(getApplicationContext(), "addOnNegativeButtonClickListener", Toast.LENGTH_SHORT).show();
            updateRecyclerView();
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (isSpinnerTouched) {
            if (position > 0) {
                roomFilter = spinner.getSelectedItem().toString();
                Toast.makeText(getApplicationContext(), "position: " + position + " " + roomFilter, Toast.LENGTH_SHORT).show();

                filterApply = "room";
                updateRecyclerView();
            } else {
                Toast.makeText(getApplicationContext(), "Veuillez choisir une salle", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private static void messageForListEmpty() {
        if (mMeetings.size() == 0)
            Toast.makeText(getAppContext(), "Aucune réunion trouvée", Toast.LENGTH_LONG).show();
    }

    private static Context getAppContext() {
        return MainActivity.context;
    }

    private void resetFilter() {
        filterApply = "reset";
        spinner.setSelection(0);
    }
}