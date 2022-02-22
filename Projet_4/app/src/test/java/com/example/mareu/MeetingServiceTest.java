package com.example.mareu;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.mareu.DI.DI;
import com.example.mareu.model.Meeting;
import com.example.mareu.service.MeetingApiService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

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
    private final int MEETINGS_COUNT = 3;
    Date hour = new Date();


    @Before
    public void setup() {

        service = DI.getNewInstanceApiService();
        meeting = service.getMeetings().get(0);
        hour.setTime(50400000); // 14H00
    }


    @Test
    public void addMeeting() {

        Meeting new_meeting = new Meeting(111111, "Reunion D", hour, "Peach", new ArrayList<>(Collections.singleton("test@gmail.com")));

        List<Meeting> meetings = service.getMeetings();
        assertEquals(MEETINGS_COUNT, service.getMeetings().size());

        service.createMeeting(new_meeting);
        assertEquals(MEETINGS_COUNT + 1, service.getMeetings().size());
        assertTrue(meetings.contains(new_meeting));
    }


    @Test
    public void deleteMeeting() {

        List<Meeting> meetings = service.getMeetings();

        assertEquals(MEETINGS_COUNT, meetings.size());
        assertTrue(meetings.contains(meeting));

        service.deleteMeeting(meeting);

        assertEquals(2, meetings.size());
        assertFalse(meetings.contains(meeting));
    }

    @Test
    public void FilterByRoom() {
        List<Meeting> listFiltered = service.filterMeetingByRoom("Peach");
        assertEquals(1, listFiltered.size());

        Meeting meeting_filter = listFiltered.get(0);
        assertEquals("Peach", meeting_filter.getRoom());
    }

    @Test
    public void FilterByHour() {

        List<Meeting> listFiltered = service.filterMeetingByHour(hour.getTime());
        assertEquals(1, listFiltered.size());

        Meeting meeting_filter = listFiltered.get(0);
        assertEquals(hour.getTime(), meeting_filter.getHour().getTime());
    }
}