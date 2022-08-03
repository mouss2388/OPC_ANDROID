package com.example.projet_7.repository;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.projet_7.model.Restaurant;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@SuppressLint("MissingPermission")
public class RestaurantRepository {

    private final MutableLiveData<ArrayList<Restaurant>> mutableLiveData;
    private final MutableLiveData<Restaurant> mutableLiveDataDetail;

    public RestaurantRepository() {
        this.mutableLiveData = new MutableLiveData<>();
        this.mutableLiveDataDetail = new MutableLiveData<>();
    }

    public LiveData<ArrayList<Restaurant>> getMutableLiveData() {
        return mutableLiveData;
    }

    public LiveData<Restaurant> getMutableLiveDataDetail() {
        return mutableLiveDataDetail;
    }

    public void getRestaurants(PlacesClient placesClient) {

        List<Place.Field> placeFieldsFindCurrentPlace =
                Arrays.asList(Place.Field.ID, Place.Field.TYPES,
                        Place.Field.LAT_LNG, Place.Field.ADDRESS, Place.Field.NAME);

        List<Place.Field> placeFieldsDetail =
                Arrays.asList(Place.Field.RATING, Place.Field.USER_RATINGS_TOTAL, Place.Field.OPENING_HOURS, Place.Field.PHOTO_METADATAS);

        FindCurrentPlaceRequest request =
                FindCurrentPlaceRequest.newInstance(placeFieldsFindCurrentPlace);

        final Task<FindCurrentPlaceResponse> placeResult =
                placesClient.findCurrentPlace(request);

        placeResult.addOnCompleteListener(task -> {

            ArrayList<Restaurant> restaurants = new ArrayList<>();

            FindCurrentPlaceResponse likelyPlaces = task.getResult();


            for (PlaceLikelihood p : likelyPlaces.getPlaceLikelihoods()) {
                Place place = p.getPlace();
                if (checkIfTypeOfPlaceIsARestaurant(place.getTypes())) {
                    final FetchPlaceRequest requestDetail = FetchPlaceRequest.newInstance(Objects.requireNonNull(place.getId()), placeFieldsDetail);

                    placesClient.fetchPlace(requestDetail).addOnSuccessListener((response) -> {
                        Place placeDetail = response.getPlace();

                        int userRatingTotal = placeDetail.getUserRatingsTotal() != null ? placeDetail.getUserRatingsTotal() : 0;

                        double rating = placeDetail.getRating() != null ? placeDetail.getRating() : 0.0;

                        final List<PhotoMetadata> metadata = placeDetail.getPhotoMetadatas();
                        if (metadata == null || metadata.isEmpty()) {
                            restaurants.add(
                                    new Restaurant(place.getId(), place.getName(), place.getAddress(), place.getLatLng(), rating,
                                            userRatingTotal, place.getTypes(), placeDetail.getOpeningHours(), null));
//                            if (finalCounter_restaurant == number_of_restaurant - 1) {

                                mutableLiveData.setValue(restaurants);
//                            }
                        } else {
                            final PhotoMetadata photoMetadata = metadata.get(0);
                            final FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                                    .setMaxWidth(500) // Optional.
                                    .setMaxHeight(300) // Optional.
                                    .build();

                            placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
                                Bitmap photo = fetchPhotoResponse.getBitmap();
                                restaurants.add(
                                        new Restaurant(place.getId(), place.getName(), place.getAddress(), place.getLatLng(), rating,
                                                userRatingTotal, place.getTypes(), placeDetail.getOpeningHours(), photo));
//                                if (finalCounter_restaurant == number_of_restaurant - 1) {

                                    mutableLiveData.setValue(restaurants);
//                                }
                            });
                        }
                    });
                }
            }
        });
    }


    public void getDetailRestaurant(PlacesClient placesClient, String placeId) {


        List<Place.Field> placeFieldsDetail =
                Arrays.asList(Place.Field.ID, Place.Field.ADDRESS, Place.Field.NAME, Place.Field.RATING, Place.Field.PHOTO_METADATAS, Place.Field.PHONE_NUMBER, Place.Field.WEBSITE_URI);


        // Construct a request object, passing the place ID and fields array.
        final FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFieldsDetail);


        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            Place placeDetail = response.getPlace();

            Restaurant restaurant;
            double rating = placeDetail.getRating() != null ? placeDetail.getRating() : 0.0;

            final List<PhotoMetadata> metadata = placeDetail.getPhotoMetadatas();
            if (metadata == null || metadata.isEmpty()) {
                restaurant =
                        new Restaurant(placeDetail.getId(), placeDetail.getName(), placeDetail.getAddress(), rating, placeDetail.getPhoneNumber(), placeDetail.getWebsiteUri());

                mutableLiveDataDetail.setValue(restaurant);
            } else {
                final PhotoMetadata photoMetadata = metadata.get(0);
                final FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                        .setMaxWidth(500) // Optional.
                        .setMaxHeight(300) // Optional.
                        .build();

                placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
                    Bitmap photo = fetchPhotoResponse.getBitmap();
                    Restaurant restaurantPhoto =
                            new Restaurant(placeDetail.getId(), placeDetail.getName(), placeDetail.getAddress(), null, rating,
                                    0, null, placeDetail.getOpeningHours(), photo);
                    mutableLiveDataDetail.setValue(restaurantPhoto);
                });
            }
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
