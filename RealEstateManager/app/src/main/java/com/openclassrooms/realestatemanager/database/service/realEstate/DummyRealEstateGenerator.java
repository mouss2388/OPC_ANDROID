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
                    new RealEstate(null, "Bien 1", 225_635.00, TypeRealEstate.Flat.toString(), 75, 7, 2, 1, "Appartement de type FLAT situé à Saint Lazare, comprenant 2 chambres, 1 salle de bain et 7 pièces. Bénéficie d'un emplacement idéal proche des commerces et des transports en commun. Appartement spacieux et lumineux, idéal pour une famille.", "120 Rue Saint-Lazare, 75009 Paris", false, Utils.getTodayDate(), "Ecole, commerces, métro", Currency.dollar.toString()),

                    new RealEstate(null, "Bien 2", 195_000.00, TypeRealEstate.Loft.toString(), 80, 5, 1, 1, "Loft situé à Saint Lazare, comprenant 1 chambre, 1 salle de bain et 5 pièces. Ce bien offre un style de vie contemporain avec des espaces de vie ouverts et des hauteurs sous plafond impressionnantes. Idéal pour les amoureux d'un style de vie urbain, proche des commerces et des transports en commun. Ce loft offre également un emplacement de choix pour profiter de la vie nocturne de la ville.", "16 Rue Saint-Lazare, 75009 Paris", false, Utils.getTodayDate(), "Ecole, magasin", Currency.dollar.toString()),

                    new RealEstate(1L, "Bien 3", 1_000_000.00, TypeRealEstate.Manoir.toString(), 200, 16, 5, 3, "Manoir situé à Saint Lazare, offrant une surface habitable de 200 m² avec 5 chambres, 3 salles de bain et 16 pièces. Ce bien unique allie le charme d'antan avec des équipements modernes. Il offre un espace de vie confortable et élégant, idéal pour les familles nombreuses ou pour ceux qui recherchent une propriété de prestige. Il offre un grand jardin et une vue imprenable.", "36 Rue Saint-Lazare, 75009 Paris", false, Utils.getTodayDate(), "Ecole, magasin", Currency.dollar.toString()),

                    new RealEstate(1L, "Bien 4", 450_000.00, TypeRealEstate.House.toString(), 125, 10, 3, 1, "Maison familiale située à Saint Lazare, comprenant 3 chambres, 1 salle de bain et 10 pièces. Ce bien offre un grand jardin, une vue imprenable et un emplacement idéal proche des commerces et des transports en commun. La maison est spacieuse, lumineuse et idéale pour accueillir une famille. Elle offre également un grand potentiel pour une extension ou une rénovation.", "20 Rue Druot, 75009 Paris", false, Utils.getTodayDate(), "Ecole, magasin", Currency.dollar.toString()),

                    new RealEstate(null, "Bien 5", 380_000.00, TypeRealEstate.Duplex.toString(), 40, 5, 2, 1, "Duplex de 40 m² situé à Saint-Lazare avec 2 chambres, 1 salle de bain et 5 pièces. Il offre une surface de vie confortable et une belle luminosité dans un quartier prisé de la ville. Idéal pour une petite famille ou pour un investissement locatif.", "2 Rue Saint-Lazare, 75009 Paris", false, Utils.getTodayDate(), "Ecole, magasin", Currency.dollar.toString())
            );

    static List<RealEstate> generateRealEstates() {
        return new ArrayList<>(DUMMY_REAL_ESTATES);
    }

}
