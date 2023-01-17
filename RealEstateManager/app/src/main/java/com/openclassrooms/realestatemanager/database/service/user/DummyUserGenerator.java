package com.openclassrooms.realestatemanager.database.service.user;

import com.openclassrooms.realestatemanager.database.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DummyUserGenerator {

    public static List<User> DUMMY_USERS =
            Collections.singletonList(
                    new User(
                            "https://img.nrj.fr/uG633dTL-M6C89enl_7ugMEuCK4=/600x900/medias%2F2020%2F09%2Fcasting-modern-family-2_5f5b4ec1a3af9.jpg",
                            "Phil",
                            "Dunphy",
                            "philDunphy@family.com",
                            "MODERN FAMILY"));

    static List<User> generateUsers() {
        return new ArrayList<>(DUMMY_USERS);

    }

}
