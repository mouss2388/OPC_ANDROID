package com.example.mareu.utlis;

import static com.example.mareu.controllers.activity.AddMeetingActivity.convertTimeToMillis;

import android.app.Activity;
import android.util.DisplayMetrics;

import com.example.mareu.R;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

public class CustomTimePicker {

    public final static int MEETING_DURATION = 45;

    public static int getHeightDevice(Activity context) {
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

    public static String convertMillisToStr(long millis) {
        int hour = (int) (millis / 60000) / 60;
        int minute = (int) (millis / 60000) % 60;
        StringBuilder hourTxt = new StringBuilder()
                .append(hour)
                .append(":");
        if (minute == 0) {
            hourTxt.append("00");
        } else {
            hourTxt.append(minute);
        }
        return String.valueOf(hourTxt);
    }

    public static long getHourSelectedInMilli(MaterialTimePicker materialTimePicker) {
        int hour = materialTimePicker.getHour();
        int minute = materialTimePicker.getMinute();
        return convertTimeToMillis(hour, minute);
    }

    public static long convertDurationMeetingInMilli() {
        return convertTimeToMillis(0, MEETING_DURATION);
    }
}
