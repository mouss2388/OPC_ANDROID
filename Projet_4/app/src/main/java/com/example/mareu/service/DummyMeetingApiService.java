package com.example.mareu.service;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.mareu.model.Meeting;

import java.util.List;
import java.util.stream.Collectors;

public class DummyMeetingApiService implements MeetingApiService {

    public List<Meeting> meetings = DummyMeetingGenerator.generateMeetings();

    @Override
    public List<Meeting> getMeetings() {
        return meetings;
    }

    @Override
    public List<Meeting> resetMeetings() {
        meetings = DummyMeetingGenerator.generateMeetings();
        return meetings;
    }

    @Override
    public void deleteMeeting(Meeting meeting) {
        meetings.remove(meeting);
    }

    @Override
    public void createMeeting(Meeting meeting) {
        meetings.add(meeting);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public List<Meeting> filterMeetingByHour(long hour) {
        return getMeetings().stream().filter(m -> m.getHour().getTime() == hour).collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public List<Meeting> filterMeetingByRoom(String room) {
        return getMeetings().stream().filter(m -> m.getRoom().equals(room)).collect(Collectors.toList());
    }
}
