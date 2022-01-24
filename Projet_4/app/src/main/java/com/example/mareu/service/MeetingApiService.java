package com.example.mareu.service;

import com.example.mareu.model.Meeting;

import java.util.List;

public interface MeetingApiService {



    List<Meeting> getReunions();
    List<Meeting> resetReunions();
    List<Meeting> filterMeetingByHour(long hourly);
    List<Meeting> filterMeetingByRoom(String room);

    void createReunion(Meeting meeting);
    void deleteReunion(Meeting meeting);


}
