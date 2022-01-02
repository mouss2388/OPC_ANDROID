package com.example.mareu.service;

import com.example.mareu.model.Reunion;

import java.util.List;

public interface ReunionApiService {

    List<Reunion> getReunions();

    void deleteReunion(Reunion reunion);

    void createReunion(Reunion reunion);
}
