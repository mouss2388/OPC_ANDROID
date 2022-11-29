package com.openclassrooms.realestatemanager.repository;

import android.app.Application;

import com.openclassrooms.realestatemanager.database.AppDatabase;
import com.openclassrooms.realestatemanager.database.dao.UserDao;
import com.openclassrooms.realestatemanager.database.model.User;

public class UserRepository {

    private final UserDao userDao;

    public UserRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        userDao = db.userDao();

    }

    public long insert(User user) {
        return userDao.insert(user);
    }

    public boolean checkIfEmailExistYet(String email) {
        return userDao.getEmail(email) != null;
    }

    public boolean checkIfPasswordIsCorrect(User user) {
        String passwordFromDb = userDao.getPassword(user.getEmail());
        if (passwordFromDb == null) {
            return false;
        } else {

            return passwordFromDb.equals(user.getPassword());
        }
    }

    public User getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    public User getUserById(long id) {
        return userDao.getUserById(id);
    }
}
