package com.example.mareu.controllers.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mareu.R;
import com.example.mareu.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        this.configureToolbar();
        mBinding.btnAddReu.setOnClickListener(v -> btn_add());
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

    private void configureToolbar() {
        setSupportActionBar(mBinding.includeToolbar.toolbar);
    }


    public void btn_add() {
        Log.i(TAG, "click on btn_add");
    }
}