package com.example.mareu.service;

import com.example.mareu.model.Reunion;

import java.util.List;

public class DummyReunionApiService implements ReunionApiService{

    public final List<Reunion> reunions =  ReunionGenerator.generateReunions();
    @Override
    public List<Reunion> getReunions() {
        return reunions;
    }

    @Override
    public void deleteReunion(Reunion reunion) {
        reunions.remove(reunion);
    }

    @Override
    public void createReunion(Reunion reunion) {
        reunions.add(reunion);

    }
}
