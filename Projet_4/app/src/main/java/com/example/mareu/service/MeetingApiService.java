package com.example.mareu.service;

import com.example.mareu.model.Meeting;

import java.util.List;

public interface MeetingApiService {

    List<Meeting> getMeetings();

    List<Meeting> resetMeetings();

    List<Meeting> filterMeetingByHour(long hourly);

    List<Meeting> filterMeetingByRoom(String room);

    void createMeeting(Meeting meeting);

    void deleteMeeting(Meeting meeting);

}
