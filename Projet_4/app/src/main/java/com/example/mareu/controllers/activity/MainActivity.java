package com.example.mareu.controllers.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mareu.DI.DI;
import com.example.mareu.R;
import com.example.mareu.databinding.ActivityMainBinding;
import com.example.mareu.model.Reunion;
import com.example.mareu.service.ReunionApiService;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();

    private ActivityMainBinding mBinding;
    private ReunionApiService mReunionApiService = DI.getReunionApiService();
    private ArrayList<Reunion> mReunions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUI();
        this.configureToolbar();

        mBinding.btnAddReu.setOnClickListener(v -> launchAddMeetingActivity());
    }

    private void initUI() {
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);
        initData();

    }

    private void initData() {
        mReunions = new ArrayList<>(mReunionApiService.getReunions());
        Log.i(TAG, mReunions.toString());
    }

    private void configureToolbar() {
        setSupportActionBar(mBinding.includeToolbar.toolbar);
    }


    public void launchAddMeetingActivity() {

        Log.i(TAG, "click on btn_add");
        startActivity(new Intent(MainActivity.this,AddMeetingActivity.class));
    }

    ////////////MENU/////////////

    /**
     * @param menu which replace by menu_activity_main
     * @return
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
}