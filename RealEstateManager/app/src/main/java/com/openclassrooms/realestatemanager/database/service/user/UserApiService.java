package com.openclassrooms.realestatemanager.database.service.user;


import com.openclassrooms.realestatemanager.database.model.User;

import java.util.List;


/**
 * Mail API client
 */
public interface UserApiService {

    /**
     * Get all my Mails
     *
     * @return {@link List}
     */
    List<User> getUsers();

}
