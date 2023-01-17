package com.openclassrooms.realestatemanager.database.service.user;


import com.openclassrooms.realestatemanager.database.model.User;

import java.util.List;

/**
 * Dummy mock for the Api
 */
public class DummyUserApiService implements UserApiService {

    private final List<User> users = DummyUserGenerator.generateUsers();


    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public List<User> getUsers() {
        return users;
    }


}
