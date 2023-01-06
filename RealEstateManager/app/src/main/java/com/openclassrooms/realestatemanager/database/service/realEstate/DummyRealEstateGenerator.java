package com.openclassrooms.realestatemanager.database.service.realEstate;

import com.openclassrooms.realestatemanager.database.enumeration.Currency;
import com.openclassrooms.realestatemanager.database.enumeration.TypeRealEstate;
import com.openclassrooms.realestatemanager.database.model.RealEstate;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DummyRealEstateGenerator {

    public static List<RealEstate> DUMMY_REAL_ESTATES =
            Arrays.asList(
                    new RealEstate(null, "Bien 1", 1_000.00, TypeRealEstate.Loft.toString(), 40, 10, 2, 5, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed sit amet egestas eros. Fusce feugiat suscipit purus sit amet ullamcorper. Nam sollicitudin mattis tincidunt. Vestibulum et pharetra justo", "address", false, Utils.getTodayDate(),"Ecole,magasin", Currency.dollar.toString()),

                    new RealEstate(1L, "Bien 2", 10_000.00, TypeRealEstate.Manoir.toString(), 40, 10, 2,4, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed sit amet egestas eros. Fusce feugiat suscipit purus sit amet ullamcorper. Nam sollicitudin mattis tincidunt. Vestibulum et pharetra justo", "address", false, Utils.getTodayDate(),"Ecole, magasin",Currency.dollar.toString())
                    ,

                    new RealEstate(null, "Bien 3", 129_345.00, TypeRealEstate.House.toString(), 40, 10, 2,3, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed sit amet egestas eros. Fusce feugiat suscipit purus sit amet ullamcorper. Nam sollicitudin mattis tincidunt. Vestibulum et pharetra justo", "address", false, Utils.getTodayDate(),"Ecole, magasin",Currency.dollar.toString())
            );

    static List<RealEstate> generateRealEstates() {
        return new ArrayList<>(DUMMY_REAL_ESTATES);
    }

}
