package com.example.projet_7.repository;

import android.annotation.SuppressLint;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.projet_7.model.Restaurant;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressLint("MissingPermission")
public class RestaurantRepository {

    private final MutableLiveData<ArrayList<Restaurant>> mutableLiveData;

    public RestaurantRepository() {
        this.mutableLiveData = new MutableLiveData<>();
    }

    public LiveData<ArrayList<Restaurant>> getMutableLiveData() {
        return mutableLiveData;
    }

    public void getRestaurants(PlacesClient placesClient) {

        List<Place.Field> placeFieldsFindCurrentPlace =
                Arrays.asList(Place.Field.ID, Place.Field.TYPES,
                        Place.Field.LAT_LNG, Place.Field.ADDRESS, Place.Field.NAME);

        // Use the builder to create a FindCurrentPlaceRequest.
        FindCurrentPlaceRequest request =
                FindCurrentPlaceRequest.newInstance(placeFieldsFindCurrentPlace);

        final Task<FindCurrentPlaceResponse> placeResult =
                placesClient.findCurrentPlace(request);

        placeResult.addOnCompleteListener(task -> {

            ArrayList<Restaurant> restaurants = new ArrayList<>();

            FindCurrentPlaceResponse likelyPlaces = task.getResult();
            for (PlaceLikelihood p : likelyPlaces.getPlaceLikelihoods()) {

                if (checkIfTypeOfPlaceIsARestaurant(p.getPlace().getTypes())) {

                    restaurants.add(
                            new Restaurant(p.getPlace().getId(),p.getPlace().getName(), p.getPlace().getAddress(), p.getPlace().getLatLng(), p.getPlace().getTypes()
                            ));
                }
            }
            mutableLiveData.setValue(restaurants);
        });
    }

    private boolean checkIfTypeOfPlaceIsARestaurant(List<Place.Type> types) {

        if (types == null) {
            return false;
        }
        List<Place.Type> typeMeal = Arrays.asList(Place.Type.RESTAURANT, Place.Type.MEAL_TAKEAWAY, Place.Type.FOOD);

        for (Place.Type type : types) {
            if (typeMeal.contains(type)) {
                return true;
            }
        }
        return false;
    }
}
