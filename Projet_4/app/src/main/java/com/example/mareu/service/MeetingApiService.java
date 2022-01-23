package com.example.mareu.service;

import com.example.mareu.model.Meeting;

import java.util.List;

public interface MeetingApiService {

    List<Meeting> getReunions();

    void deleteReunion(Meeting meeting);

    void createReunion(Meeting meeting);
}
