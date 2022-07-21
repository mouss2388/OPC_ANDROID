package com.example.projet_7.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.OpeningHours;
import com.google.android.libraries.places.api.model.Place;

import java.util.List;

public class Restaurant {

    private String id;
    private String name;
    private String address;
    private LatLng latLng;
    private OpeningHours openingHours;
    private String phoneNumber;
    private String photo;
    private double rating;
    private int userRatingTotal;
    private boolean isOpen;
    private List<Place.Type> types;

    public Restaurant(String id){
        this.id = id;
    }
    public Restaurant(String id,String name, String address, LatLng latLng, List<Place.Type> types) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.latLng = latLng;
        this.types = types;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(OpeningHours openingHours) {
        this.openingHours = openingHours;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getUserRatingTotal() {
        return userRatingTotal;
    }

    public void setUserRatingTotal(int userRatingTotal) {
        this.userRatingTotal = userRatingTotal;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public List<Place.Type> getTypes() {
        return types;
    }

    public void setTypes(List<Place.Type> types) {
        this.types = types;
    }
}
