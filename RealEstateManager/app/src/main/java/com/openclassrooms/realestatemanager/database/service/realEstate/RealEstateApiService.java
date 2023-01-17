package com.openclassrooms.realestatemanager.database.service.realEstate;


import com.openclassrooms.realestatemanager.database.model.RealEstate;

import java.util.List;


/**
 * Mail API client
 */
public interface RealEstateApiService {

    /**
     * Get all my Mails
     *
     * @return {@link List}
     */
    List<RealEstate> getRealEstates();

}
