package com.openclassrooms.realestatemanager.database.service.image;


import com.openclassrooms.realestatemanager.database.model.Image;

import java.util.List;

/**
 * Dummy mock for the Api
 */
public class DummyImageApiService implements ImageApiService {

    private final List<Image> images = DummyImageGenerator.generateImagess();


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Image> getImages() {
        return images;
    }


}
