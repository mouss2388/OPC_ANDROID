package com.example.mareu.utlis;

import android.app.Activity;
import android.util.DisplayMetrics;

import com.example.mareu.R;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

public class CustomTimePicker  {

    public  static int getHeightDevice(Activity context) {
        //GET DIMENSION DEVICE
        DisplayMetrics metrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }


    public static MaterialTimePicker getTimePicker(Activity context) {
        int timeMode = getHeightDevice(context) < 720 ? MaterialTimePicker.INPUT_MODE_KEYBOARD : MaterialTimePicker.INPUT_MODE_CLOCK;
         return new
                MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setInputMode(timeMode)
                .setTitleText(R.string.hour_meeting)
                .setHour(0)
                .setMinute(0)
                .build();
    }
}
