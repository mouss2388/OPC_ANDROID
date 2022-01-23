package com.example.mareu.service;

import com.example.mareu.model.Meeting;

import java.util.List;

public class DummyMeetingApiService implements MeetingApiService {

    public final List<Meeting> mMeetings =  DummyMeetingGenerator.generateReunions();
    @Override
    public List<Meeting> getReunions() {
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
}
