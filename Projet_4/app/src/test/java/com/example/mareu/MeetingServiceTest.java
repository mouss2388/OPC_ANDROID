package com.example.mareu;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

import com.example.mareu.model.Meeting;
import com.example.mareu.service.MeetingApiService;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(JUnit4.class)
public class MeetingServiceTest {

    private MeetingApiService service;
    private Meeting meeting;
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
}