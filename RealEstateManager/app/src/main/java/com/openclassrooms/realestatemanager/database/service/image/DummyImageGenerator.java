package com.openclassrooms.realestatemanager.database.service.image;

import com.openclassrooms.realestatemanager.database.model.Image;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DummyImageGenerator {

    public static List<Image> DUMMY_IMAGES =
            Arrays.asList(

                    new Image(1L, "https://cdn.pixabay.com/photo/2016/11/18/17/46/house-1836070_1280.jpg"),
                    new Image(2L, "https://cdn.pixabay.com/photo/2017/05/31/10/23/manor-house-2359884_1280.jpg"),
                    new Image(2L, "https://cdn.pixabay.com/photo/2017/05/31/10/23/manor-house-2359884_1280.jpg"),
                    new Image(2L, "https://cdn.pixabay.com/photo/2017/05/31/10/23/manor-house-2359884_1280.jpg"),
                    new Image(2L, "https://cdn.pixabay.com/photo/2017/05/31/10/23/manor-house-2359884_1280.jpg"),
                    new Image(2L, "https://cdn.pixabay.com/photo/2017/05/31/10/23/manor-house-2359884_1280.jpg"),
                    new Image(2L, "https://cdn.pixabay.com/photo/2017/05/31/10/23/manor-house-2359884_1280.jpg"),
                    new Image(2L, "https://cdn.pixabay.com/photo/2017/05/31/10/23/manor-house-2359884_1280.jpg"),
                    new Image(2L, "https://cdn.pixabay.com/photo/2017/05/31/10/23/manor-house-2359884_1280.jpg"),
                    new Image(2L, "https://cdn.pixabay.com/photo/2017/05/31/10/23/manor-house-2359884_1280.jpg"),
                    new Image(1L, "https://cdn.pixabay.com/photo/2016/09/26/11/26/resort-1695717_1280.jpg"),
                    new Image(3L, "https://cdn.pixabay.com/photo/2016/11/18/17/46/house-1836070_1280.jpg")
            );

    static List<Image> generateImages() {
        return new ArrayList<>(DUMMY_IMAGES);
    }

}
