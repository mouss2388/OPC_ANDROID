package com.example.projet_7.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.projet_7.model.Restaurant;
import com.example.projet_7.repository.RestaurantRepository;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;

public class RestaurantViewModel extends ViewModel {

    private final RestaurantRepository restaurantRepository;

    public RestaurantViewModel() {
        restaurantRepository = new RestaurantRepository();
    }


    public LiveData<ArrayList<Restaurant>> getLiveData() {
        return restaurantRepository.getMutableLiveData();
    }

    public void getRestaurants(PlacesClient placesClient) {
        restaurantRepository.getRestaurants(placesClient);
    }

    public LiveData<Restaurant> getLiveDataDetail() {
        return restaurantRepository.getMutableLiveDataDetail();
    }

    public void getRestaurantDetail(PlacesClient placesClient, String placeId) {
        restaurantRepository.getDetailRestaurant(placesClient, placeId);
    }


}
