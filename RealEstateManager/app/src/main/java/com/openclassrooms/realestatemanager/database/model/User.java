package com.openclassrooms.realestatemanager.database.model;

import android.content.ContentValues;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_table")
public class User {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    @ColumnInfo(name = "firstname")
    public String firstname;

    @NonNull
    @ColumnInfo(name = "lastname")
    public String lastname;

    @NonNull
    @ColumnInfo(name = "password")
    public String password;


    @NonNull
    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "picture")
    public String picture;

    @Ignore
    public User() {
    }

    public User(String picture, @NonNull String firstname, @NonNull
            String lastname, @NonNull String email, @NonNull String password) {
        this.picture = picture;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }

    public long getId() {
        return id;
    }


    @NonNull
    public String getFirstname() {
        return firstname;
    }

    @NonNull
    public String getLastname() {
        return lastname;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setFirstname(@NonNull String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(@NonNull String lastname) {
        this.lastname = lastname;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    @Nullable
    public String getPicture() {
        return picture;
    }

    public void setPicture(@Nullable String picture) {
        this.picture = picture;
    }

    public static User fromContentValues(ContentValues values) {

        final User user = new User();

        if (values.containsKey("id")) user.setId(values.getAsLong("id"));

        if (values.containsKey("firstname")) user.setFirstname(values.getAsString("firstname"));
        if (values.containsKey("lastname")) user.setLastname(values.getAsString("lastname"));

        if (values.containsKey("email")) user.setEmail(values.getAsString("email"));


        if (values.containsKey("password")) user.setPassword(values.getAsString("password"));

        return user;
    }
}
