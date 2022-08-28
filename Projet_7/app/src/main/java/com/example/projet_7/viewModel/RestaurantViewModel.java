package com.example.projet_7.viewModel;

import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.projet_7.model.Restaurant;
import com.example.projet_7.model.User;
import com.example.projet_7.repository.RestaurantRepository;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;

public class RestaurantViewModel extends ViewModel {

    private final RestaurantRepository restaurantRepository;

    public RestaurantViewModel() {
        restaurantRepository = new RestaurantRepository();
    }


    public LiveData<ArrayList<Restaurant>> getLiveDataRestaurantsAroundMe() {
        return restaurantRepository.getLiveDataRestaurantsAroundMe();
    }

    public void getRestaurantsAroundMe(PlacesClient placesClient) {
        restaurantRepository.getRestaurantsAroundMe(placesClient);
    }

    public LiveData<Restaurant> getLiveDataRestaurantDetail() {
        return restaurantRepository.getLiveDataRestaurantDetail();
    }

    public void getDetailsRestaurant(PlacesClient placesClient, String placeId) {
        restaurantRepository.getDetailsRestaurant(placesClient, placeId);
    }

    public LiveData<ArrayList<Restaurant>> getLiveDataRestaurantBooked() {
        return restaurantRepository.getLiveDataRestaurantBooked();
    }

    public void getRestaurantsBooked(ArrayList<User> workmates, PlacesClient placesClient) {
        restaurantRepository.getRestaurantsBooked(workmates, placesClient);
    }

    public LiveData<ArrayList<Restaurant>> getLiveDataRestaurantsByPrediction() {
        return restaurantRepository.getLiveDataRestaurantsByPrediction();
    }

    public void getRestaurantsByPrediction(Location currentLocation, String query) {
        restaurantRepository.getRestaurantsByPrediction(currentLocation, query);
    }


}
