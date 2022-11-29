package com.openclassrooms.realestatemanager.database.service.realEstate;

import com.openclassrooms.realestatemanager.database.enumeration.TypeRealEstate;
import com.openclassrooms.realestatemanager.database.model.RealEstate;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DummyRealEstateGenerator {

    public static List<RealEstate> DUMMY_REAL_ESTATES =
            Arrays.asList(
                    new RealEstate("Bien 1", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce iaculis erat at nunc bibendum, ac varius tellus feugiat. Praesent mi odio, molestie non vehicula a, dictum et lacus. Quisque pellentesque arcu justo. Quisque malesuada placerat odio, interdum placerat dui. Integer nulla purus, lacinia in commodo non, luctus eget ante. Nunc dignissim auctor nisl, nec porta dui ullamcorper viverra. Quisque elementum nisl id orci interdum, non dignissim odio sodales. Fusce ut malesuada enim, ac gravida mi. Nunc porta nisl at tortor mattis varius.", "13 rue des batignolles 75000 Paris", 123_000, TypeRealEstate.Penthouse, 65, 6, 2, false, Utils.getTodayDate()),

                    new RealEstate("Bien 2", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce iaculis erat at nunc bibendum, ac varius tellus feugiat. Praesent mi odio, molestie non vehicula a, dictum et lacus. Quisque pellentesque arcu justo. Quisque malesuada placerat odio, interdum placerat dui. Integer nulla purus, lacinia in commodo non, luctus eget ante. Nunc dignissim auctor nisl, nec porta dui ullamcorper viverra. Quisque elementum nisl id orci interdum, non dignissim odio sodales. Fusce ut malesuada enim, ac gravida mi. Nunc porta nisl at tortor mattis varius.", "13 rue des batignolles 75000 Paris", 485_321, TypeRealEstate.Flat, 65, 6, 2, false, Utils.getTodayDate()),

                    new RealEstate("Bien 3", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce iaculis erat at nunc bibendum, ac varius tellus feugiat. Praesent mi odio, molestie non vehicula a, dictum et lacus. Quisque pellentesque arcu justo. Quisque malesuada placerat odio, interdum placerat dui. Integer nulla purus, lacinia in commodo non, luctus eget ante. Nunc dignissim auctor nisl, nec porta dui ullamcorper viverra. Quisque elementum nisl id orci interdum, non dignissim odio sodales. Fusce ut malesuada enim, ac gravida mi. Nunc porta nisl at tortor mattis varius.", "13 rue des batignolles 75000 Paris", 1_000_000, TypeRealEstate.Manoir, 65, 6, 2, false, Utils.getTodayDate())
            );

    static List<RealEstate> generateRealEstates() {
        return new ArrayList<>(DUMMY_REAL_ESTATES);
    }

}
