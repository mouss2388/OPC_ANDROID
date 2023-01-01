package com.openclassrooms.realestatemanager.database.service.image;


import com.openclassrooms.realestatemanager.database.model.Image;

import java.util.List;


/**
 * Mail API client
 */
public interface ImageApiService {

    /**
     * Get all my Mails
     *
     * @return {@link List}
     */
    List<Image> getImages();

}
