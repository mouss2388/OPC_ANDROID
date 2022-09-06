package com.example.projet_7.model;

import androidx.annotation.Nullable;

@SuppressWarnings("unused")
public class User {

    private String uid;
    private String username;
    @Nullable
    private String urlPicture;

    private String restaurantBookedId;

    public User() {
    }


    public User(String uid, String username, @Nullable String urlPicture, String restaurantBookedId) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
        this.restaurantBookedId = restaurantBookedId;
    }


    // --- GETTERS ---
    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    @Nullable
    public String getUrlPicture() {
        return urlPicture;
    }

    // --- SETTERS ---
    public void setUsername(String username) {
        this.username = username;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUrlPicture(@Nullable String urlPicture) {
        this.urlPicture = urlPicture;
    }

    public String getRestaurantBookedId() {
        return restaurantBookedId;
    }

    public void setRestaurantBookedId(String restaurantBookedId) {
        this.restaurantBookedId = restaurantBookedId;
    }
}
