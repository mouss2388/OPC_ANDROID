package com.example.mareu.service;

import android.os.Build;

import com.example.mareu.model.Meeting;

import java.util.ArrayList;
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

    @Override
    public List<Meeting> filterMeetingByHour(long hour) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return getMeetings().stream().filter(m -> m.getHour().getTime() == hour).collect(Collectors.toList());
        } else {
            List<Meeting> meetings = new ArrayList<>();
            for (Meeting meeting : getMeetings()) {
                if (meeting.getHour().getTime() == hour) {
                    meetings.add(meeting);
                }
            }
            return meetings;
        }
    }

    @Override
    public List<Meeting> filterMeetingByRoom(String room) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            return getMeetings().stream().filter(m -> m.getRoom().equals(room)).collect(Collectors.toList());
        }else{
            List<Meeting> meetings = new ArrayList<>();
            for (Meeting meeting : getMeetings()) {
                if (meeting.getRoom().equals(room)) {
                    meetings.add(meeting);
                }
            }
            return meetings;
        }
    }
}
