package com.example.mareu.service;

import com.example.mareu.model.Meeting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DummyMeetingGenerator {

    static Date hour_meeting_1 = new Date();
    static Date hour_meeting_2 = new Date();
    static Date hour_meeting_3 = new Date();

    private static void initDate() {
        hour_meeting_1.setTime(57600000);
        hour_meeting_2.setTime(50400000);
        hour_meeting_3.setTime(0);
    }


    public static List<Meeting> DUMMY_MEETINGS = Arrays.asList(
            new Meeting(-25143, "Réunion A", hour_meeting_1, "Peach", new ArrayList<>(Collections.singleton("maxime@lamzone.com, alex@lamzome.com"))),
            new Meeting(-61180, "Réunion B", hour_meeting_2, "Mario", new ArrayList<>(Collections.singleton("paul@lamzome.com, viviane@lamzone.com"))),
            new Meeting(-16580839, "Réunion C", hour_meeting_3, "Luigi", new ArrayList<>(Collections.singleton("amandine@lamzome.com, luc@lamzone.com")))
    );


    static List<Meeting> generateMeetings() {
        initDate();
        return new ArrayList<>(DUMMY_MEETINGS);
    }

    ;

}
