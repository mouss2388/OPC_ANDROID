package com.example.projet_7.repository;

import static com.example.projet_7.ui.MainActivity.placesClient;
import static com.example.projet_7.utils.Utils.getIdsOfRestaurantsWithoutDuplicate;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.projet_7.model.Restaurant;
import com.example.projet_7.model.User;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@SuppressLint("MissingPermission")
public class RestaurantRepository {

    private final MutableLiveData<ArrayList<Restaurant>> muLivDataRestaurantsAroundMe;
    private final MutableLiveData<Restaurant> muLivDataRestaurantDetail;
    private final MutableLiveData<ArrayList<Restaurant>> muLivDataRestaurantsBooked;
    private final MutableLiveData<ArrayList<Restaurant>> muLivDataRestaurantsPrediction;

    public RestaurantRepository() {
        this.muLivDataRestaurantsAroundMe = new MutableLiveData<>();
        this.muLivDataRestaurantDetail = new MutableLiveData<>();
        this.muLivDataRestaurantsBooked = new MutableLiveData<>();
        this.muLivDataRestaurantsPrediction = new MutableLiveData<>();
    }

    public LiveData<ArrayList<Restaurant>> getLiveDataRestaurantsAroundMe() {
        return muLivDataRestaurantsAroundMe;
    }

    public LiveData<Restaurant> getLiveDataRestaurantDetail() {
        return muLivDataRestaurantDetail;
    }

    public LiveData<ArrayList<Restaurant>> getLiveDataRestaurantBooked() {
        return muLivDataRestaurantsBooked;
    }

    public LiveData<ArrayList<Restaurant>> getLiveDataRestaurantsByPrediction() {
        return muLivDataRestaurantsPrediction;
    }

    public void getRestaurantsAroundMe(PlacesClient placesClient) {

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

                            muLivDataRestaurantsAroundMe.postValue(restaurants);
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

                                muLivDataRestaurantsAroundMe.postValue(restaurants);

                            });
                        }
                    });
                }
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

    public void getDetailsRestaurant(PlacesClient placesClient, String placeId) {


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

                muLivDataRestaurantDetail.postValue(restaurant);
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
                    muLivDataRestaurantDetail.postValue(restaurantPhoto);
                });
            }
        });
    }

    public void getRestaurantsBooked(ArrayList<User> workmates, PlacesClient placesClient) {

        ArrayList<Restaurant> restaurantsBooked = new ArrayList<>();

        Set<String> uids = getIdsOfRestaurantsWithoutDuplicate(workmates);

        List<Place.Field> placeFieldsDetail =
                Arrays.asList(Place.Field.ID, Place.Field.NAME);

        for (String uid : uids) {
            // Construct a request object, passing the place ID and fields array.
            final FetchPlaceRequest request = FetchPlaceRequest.newInstance(uid, placeFieldsDetail);
            placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
                Place placeDetail = response.getPlace();
                Restaurant restaurant = new Restaurant(placeDetail.getId(), placeDetail.getName());
                restaurantsBooked.add(restaurant);
                muLivDataRestaurantsBooked.postValue(restaurantsBooked);
            });
        }
    }

    public void getRestaurantsByPrediction(Location currentLocation, String text) {

        List<Place.Field> placeFieldsDetail =
                Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.ADDRESS, Place.Field.NAME, Place.Field.RATING, Place.Field.PHOTO_METADATAS, Place.Field.PHONE_NUMBER, Place.Field.WEBSITE_URI, Place.Field.USER_RATINGS_TOTAL);
        // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
        // and once again when the user makes a selection (for example when calling fetchPlace()).
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        ArrayList<Restaurant> restaurants = new ArrayList<>();
        double radius = 1.0;
        double[] radiusBounds = getBoundsFromLatLng(radius, currentLocation.getLatitude(), currentLocation.getLongitude());

        // Create a RectangularBounds object.
        RectangularBounds bounds = RectangularBounds.newInstance(
                new LatLng(radiusBounds[0], radiusBounds[1]),
                new LatLng(radiusBounds[2], radiusBounds[3]));

        // Use the builder to create a FindAutocompletePredictionsRequest.
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                // Call either setLocationBias() OR setLocationRestriction().
                .setLocationRestriction(bounds)
                .setOrigin(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                .setTypeFilter(TypeFilter.ESTABLISHMENT)
                .setSessionToken(token)
                .setQuery(text)
                .build();

        placesClient.findAutocompletePredictions(request).addOnSuccessListener((response) -> {
            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                if (checkIfTypeOfPlaceIsARestaurant(prediction.getPlaceTypes())) {
                    String id = prediction.getPlaceId();
                    // Construct a request object, passing the place ID and fields array.
                    final FetchPlaceRequest requestDetail = FetchPlaceRequest.newInstance(id, placeFieldsDetail);

                    placesClient.fetchPlace(requestDetail).addOnSuccessListener((responseDetail) -> {
                        Place placeDetail = responseDetail.getPlace();

                        int userRatingTotal = placeDetail.getUserRatingsTotal() != null ? placeDetail.getUserRatingsTotal() : 0;

                        double rating = placeDetail.getRating() != null ? placeDetail.getRating() : 0.0;

                        final List<PhotoMetadata> metadata = placeDetail.getPhotoMetadatas();
                        if (metadata == null || metadata.isEmpty()) {
                            restaurants.add(
                                    new Restaurant(placeDetail.getId(), placeDetail.getName(), placeDetail.getAddress(), placeDetail.getLatLng(), rating,
                                            userRatingTotal, placeDetail.getTypes(), placeDetail.getOpeningHours(), null));

                            muLivDataRestaurantsPrediction.postValue(restaurants);
                        } else {
                            final PhotoMetadata photoMetadata = metadata.get(0);
                            final FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                                    .setMaxWidth(500) // Optional.
                                    .setMaxHeight(300) // Optional.
                                    .build();

                            placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
                                Bitmap photo = fetchPhotoResponse.getBitmap();
                                restaurants.add(
                                        new Restaurant(placeDetail.getId(), placeDetail.getName(), placeDetail.getAddress(), placeDetail.getLatLng(), rating,
                                                userRatingTotal, placeDetail.getTypes(), placeDetail.getOpeningHours(), photo));
                                muLivDataRestaurantsPrediction.postValue(restaurants);
                            });
                        }
                    }).addOnFailureListener((exception) -> {
                        if (exception instanceof ApiException) {
                            ApiException apiException = (ApiException) exception;
                            Log.e("Place", "Place not found: " + apiException.getStatusCode());
                        }
                    });
                }
            }
        });
    }


    private double[] getBoundsFromLatLng(double radius, double lat, double lng) {
        double lat_change = radius / 111.2f;
        double lon_change = Math.abs(Math.cos(lat * (Math.PI / 180)));
        return new double[]{
                lat - lat_change,
                lng - lon_change,
                lat + lat_change,
                lng + lon_change
        };
    }

}
