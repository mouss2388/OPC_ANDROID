package com.example.mareu.service;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.mareu.model.Meeting;

import java.util.List;
import java.util.stream.Collectors;

public class DummyMeetingApiService implements MeetingApiService {

    public List<Meeting> mMeetings = DummyMeetingGenerator.generateReunions();

    @Override
    public List<Meeting> getReunions() {
        return mMeetings;
    }

    @Override
    public List<Meeting> resetReunions() {
        mMeetings = DummyMeetingGenerator.generateReunions();
        return mMeetings;
    }

    @Override
    public void deleteReunion(Meeting meeting) {
        mMeetings.remove(meeting);
    }

    @Override
    public void createReunion(Meeting meeting) {
        mMeetings.add(meeting);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public List<Meeting> filterMeetingByHour(long hourly) {
        return getReunions().stream().filter(m -> m.getHour().getTime() == hourly).collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public List<Meeting> filterMeetingByRoom(String room) {
        return getReunions().stream().filter(m -> m.getRoom().equals(room)).collect(Collectors.toList());    }
}
