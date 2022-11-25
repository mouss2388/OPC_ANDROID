package com.openclassrooms.realestatemanager.database.service;


import com.openclassrooms.realestatemanager.database.model.RealEstate;

import java.util.List;

/**
 * Dummy mock for the Api
 */
public class DummyRealEstateApiService implements RealEstateApiService {

    private final List<RealEstate> realEstates = DummyRealEstateGenerator.generateRealEstates();


    /**
     * {@inheritDoc}
     */
    @Override
    public List<RealEstate> getRealEstates() {
        return realEstates;
    }


}
