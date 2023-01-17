package com.openclassrooms.realestatemanager.database.service.geocoding;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GeocodingApiClient {

    public static String API_URL = "https://maps.googleapis.com/maps/api/geocode/";

    public static Retrofit getClient() {
        return new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static GeocodingApiService geocodingApiService() {
        return getClient().create(GeocodingApiService.class);
    }
}
