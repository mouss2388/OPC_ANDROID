package com.openclassrooms.realestatemanager.utils;

import com.openclassrooms.realestatemanager.database.model.RealEstate;
import com.openclassrooms.realestatemanager.database.model.geocoding_api.ResultsItem;

import java.util.List;

public interface OnGeocodingApiReceivedCallback {
    void onGeocodingApiReceivedCallback(List<ResultsItem> addresses, RealEstate realEstate, String addOrUpdate);

}
