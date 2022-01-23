package com.example.mareu.service;

import com.example.mareu.model.Meeting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DummyMeetingGenerator {

    static Date date_meeting_1 = new Date();
    static Date date_meeting_2 = new Date();
    static Date date_meeting_3 = new Date();

    private static void initDate() {
    date_meeting_1.setTime(50400000);
    date_meeting_2.setTime(57600000);
    date_meeting_3.setTime(68400000);
    }


    public static List<Meeting> DUMMY_MEETINGS = Arrays.asList(
            new Meeting(-111111, "Réunion A", date_meeting_1, "Peach", new ArrayList<>(Collections.singleton("maxime@lamzone.com, alex@lamzome.com"))),
            new Meeting(-111111, "Réunion B", date_meeting_2, "Mario", new ArrayList<>(Collections.singleton("paul@lamzome.com, viviane@lamzone.com"))),
            new Meeting(-111111, "Réunion C", date_meeting_3, "Luigi", new ArrayList<>(Collections.singleton("amandine@lamzome.com, luc@lamzone.com")))
    );


    static List<Meeting> generateReunions() {
        initDate();
        return new ArrayList<>(DUMMY_MEETINGS);
    }

    ;

}
