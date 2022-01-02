package com.example.mareu.model;

import java.util.ArrayList;
import java.util.Date;

public class Reunion {
    private String subject;
    private Date hour;
    private String room;
    private ArrayList<String> emails;

    public Reunion(String subject, Date hour, String room, ArrayList<String> emails) {
        this.subject = subject;
        this.hour = hour;
        this.room = room;
        this.emails = emails;
    }

    @Override
    public String toString() {
        return "Reunion{" +
                "subject='" + subject + '\'' +
                ", hour=" + hour +
                ", room='" + room + '\'' +
                ", emails=" + emails +
                '}';
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Date getHour() {
        return hour;
    }

    public void setHour(Date hour) {
        this.hour = hour;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public ArrayList<String> getEmails() {
        return emails;
    }

    public void setEmails(ArrayList<String> emails) {
        this.emails = emails;
    }
}
