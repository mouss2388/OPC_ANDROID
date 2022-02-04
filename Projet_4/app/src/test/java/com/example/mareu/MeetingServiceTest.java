package com.example.mareu;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

import com.example.mareu.DI.DI;
import com.example.mareu.model.Meeting;
import com.example.mareu.service.DummyMeetingGenerator;
import com.example.mareu.service.MeetingApiService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(JUnit4.class)
public class MeetingServiceTest {

    private MeetingApiService service;
    private Meeting meeting;
    private int MEETINGS_COUNT = 3;
    Date hour = new Date();



    @Before
    public void setup() {
        service = DI.getNewInstanceApiService();
        meeting = service.getReunions().get(0);
        hour.setTime(50400000); // 14H00
    }


    @Test
    public void getMeeting() {
        List<Meeting> meetings = DummyMeetingGenerator.DUMMY_MEETINGS;
        assertTrue(meetings.contains(meeting));
    }

    @Test
    public void addMeeting() {


        Meeting new_meeting = new Meeting(111111, "Reunion D", hour, "Peach", new ArrayList<>(Collections.singleton("test@gmail.com")));

        List<Meeting> meetings = service.getReunions();
        assertEquals(MEETINGS_COUNT, service.getReunions().size());

        service.createReunion(new_meeting);
        assertEquals(MEETINGS_COUNT + 1, service.getReunions().size());
        assertTrue(meetings.contains(new_meeting));
    }


    @Test
    public void deleteMeeting() {
        List<Meeting> meetings = service.getReunions();

        assertEquals(3, meetings.size());
        assertTrue(meetings.contains(meeting));

        service.deleteReunion(meeting);

        assertEquals(2, meetings.size());
        assertFalse(meetings.contains(meeting));
    }

    @Test
    public void FilterByRoom() {
        Meeting meeting_filter = service.filterMeetingByRoom("Peach").get(0);
        assertEquals("Peach",meeting_filter.getRoom());
    }

    @Test
    public void FilterByHour() {

        Meeting meeting_filter = service.filterMeetingByHour(hour.getTime()).get(0);

        assertEquals(hour.getTime(),meeting_filter.getHour().getTime());
    }
}