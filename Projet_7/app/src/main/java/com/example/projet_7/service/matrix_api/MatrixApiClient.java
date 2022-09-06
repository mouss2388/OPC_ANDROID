package com.example.projet_7.service.matrix_api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MatrixApiClient {

    public static String API_URL = "https://maps.googleapis.com/maps/api/distancematrix/";

    public static Retrofit getClient() {
        return new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static MatrixApiService matrixApiService() {
        return getClient().create(MatrixApiService.class);
    }
}
