package com.openclassrooms.realestatemanager.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.openclassrooms.realestatemanager.database.model.User;

@Dao
public interface UserDao {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(User user);

    @Query("SELECT email FROM user_table WHERE email = :email")
    String getEmail(String email);

    @Query("SELECT password FROM user_table WHERE email = :email")
    String getPassword(String email);

}
