package com.openclassrooms.realestatemanager.database.service.image;

import com.openclassrooms.realestatemanager.database.model.Image;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DummyImageGenerator {

    public static List<Image> DUMMY_IMAGES =
            Collections.singletonList(

                    new Image(2L, "https://cdn.pixabay.com/photo/2017/05/31/10/23/manor-house-2359884_1280.jpg")
            );

    static List<Image> generateImagess() {
        return new ArrayList<>(DUMMY_IMAGES);
    }

}
