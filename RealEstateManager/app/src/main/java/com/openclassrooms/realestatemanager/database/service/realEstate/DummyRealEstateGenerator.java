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
                    new RealEstate(null, "Bien 1", 1_000, TypeRealEstate.Loft, 40, 10, 2, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed sit amet egestas eros. Fusce feugiat suscipit purus sit amet ullamcorper. Nam sollicitudin mattis tincidunt. Vestibulum et pharetra justo", "address", false, Utils.getTodayDate()),

                    new RealEstate(1L, "Bien 2", 10_000, TypeRealEstate.Manoir, 40, 10, 2, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed sit amet egestas eros. Fusce feugiat suscipit purus sit amet ullamcorper. Nam sollicitudin mattis tincidunt. Vestibulum et pharetra justo", "address", false, Utils.getTodayDate())
                    ,

                    new RealEstate(null, "Bien 3", 1_00_000, TypeRealEstate.House, 40, 10, 2, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed sit amet egestas eros. Fusce feugiat suscipit purus sit amet ullamcorper. Nam sollicitudin mattis tincidunt. Vestibulum et pharetra justo", "address", false, Utils.getTodayDate())
            );

    static List<RealEstate> generateRealEstates() {
        return new ArrayList<>(DUMMY_REAL_ESTATES);
    }

}
