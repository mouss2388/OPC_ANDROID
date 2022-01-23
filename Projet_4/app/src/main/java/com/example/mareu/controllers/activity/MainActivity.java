package com.example.mareu.controllers.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mareu.DI.DI;
import com.example.mareu.R;
import com.example.mareu.databinding.ActivityMainBinding;
import com.example.mareu.model.Meeting;
import com.example.mareu.service.MeetingApiService;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();

    private ActivityMainBinding mBinding;
    private static final MeetingApiService mMeetingApiService = DI.getReunionApiService();
    private static ArrayList<Meeting> mMeetings;
    public static MeetingAdapter meetingAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUI();
        this.configureToolbar();
        this.initRecyclerView();

        mBinding.btnAddReu.setOnClickListener(v -> launchAddMeetingActivity());
    }

    private void initUI() {
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);
        initData();

    }

    private void initData() {
        mMeetings = new ArrayList<>(mMeetingApiService.getReunions());
        Log.i(TAG, mMeetings.toString());
    }

    private void configureToolbar() {
        setSupportActionBar(mBinding.includeToolbar.toolbar);
    }


    public void launchAddMeetingActivity() {

        Log.i(TAG, "click on btn_add");
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

            case R.id.filter_date:
                Log.i(TAG, "click on date");
                return true;
            case R.id.filter_place:
                Log.i(TAG, "click on place");
                return true;
            case R.id.filter_reset:
                Log.i(TAG, "click on reset");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mBinding.recyclerView.setLayoutManager(layoutManager);
        meetingAdapter = new MeetingAdapter(mMeetings);

//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mBinding.recyclerView.getContext(),
//                layoutManager.getOrientation());
//        mBinding.recyclerView.addItemDecoration(dividerItemDecoration);

        mBinding.recyclerView.setAdapter(meetingAdapter);
    }

    public static void updateRecyclerView(){
        mMeetings.clear();
        mMeetings.addAll(mMeetingApiService.getReunions());
        meetingAdapter.notifyDataSetChanged();
    }

}