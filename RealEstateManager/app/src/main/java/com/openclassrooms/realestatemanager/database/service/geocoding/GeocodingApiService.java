package com.openclassrooms.realestatemanager.database.service.geocoding;

import com.openclassrooms.realestatemanager.database.model.geocoding_api.Response;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GeocodingApiService {

    String API_KEY = "AIzaSyDXa6QgrrPHPVEciXG0LdkZCr1-_FLP9y0";

    @GET("json?&key=" + API_KEY)
    Call<Response> getResult(@retrofit2.http.Query("address") String address);
}

