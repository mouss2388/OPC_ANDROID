package com.example.projet_7.service.matrix_api;

import com.example.projet_7.BuildConfig;
import com.example.projet_7.model.matrix_api.Response;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MatrixApiService {

    String API_KEY = BuildConfig.API_KEY;

    @GET("json?mode=walking&key=" + API_KEY)
    Call<Response> getResult(@Query("origins") StringBuilder originsLatLng, @Query("destinations") StringBuilder destinationsLatLng);
}
