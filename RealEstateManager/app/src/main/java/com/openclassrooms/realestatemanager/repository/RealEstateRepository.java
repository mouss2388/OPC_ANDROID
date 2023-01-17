package com.openclassrooms.realestatemanager.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.database.AppDatabase;
import com.openclassrooms.realestatemanager.database.dao.ImageDao;
import com.openclassrooms.realestatemanager.database.dao.RealEstateDao;
import com.openclassrooms.realestatemanager.database.model.Image;
import com.openclassrooms.realestatemanager.database.model.RealEstate;

import java.util.List;

public class RealEstateRepository {

    private final RealEstateDao realEstateDao;
    private final ImageDao imageDao;


    public RealEstateRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        realEstateDao = db.realEstateDao();
        imageDao = db.imageDao();

    }

    public long insert(RealEstate realEstate) {
        return realEstateDao.insert(realEstate);
    }


    public int update(RealEstate realEstate) {
        return realEstateDao.update(realEstate);
    }

    public LiveData<RealEstate> getRealEstateById(long id) {
        return realEstateDao.getRealEstateById(id);
    }

    public LiveData<List<RealEstate>> getAllRealEstates() {
        return realEstateDao.getAllRealEstates();
    }

    public LiveData<List<RealEstate>> getRealEstateByUserId(long id) {
        return realEstateDao.getRealEstateByUserId(id);
    }

    public LiveData<List<Image>> getRealEstateImages(RealEstate realEstate) {
        return imageDao.getRealEstateImages(realEstate.getId());
    }

    public int deleteRealEstateImages(Image image) {
        return imageDao.delete(image);
    }


    public long addRealEstateImage(Image image) {
        return imageDao.insert(image);
    }

    public int getNumberOfImages(long id) {
        return imageDao.getNumberOfImages(id);
    }

    public LiveData<List<RealEstate>> getAllRealEstatesByFilters(boolean sold, List<Float> prices, List<Float> surfaces, Integer rooms, Integer bathRooms, Integer bedRooms, Long agentId) {
        Float priceMin = prices.get(0);
        Float priceMax = prices.get(1);
        Float surfaceMin = surfaces.get(0);
        Float surfaceMax = surfaces.get(1);
        if (surfaceMax == 0.0) {
            surfaceMin = null;
            surfaceMax = null;
        }
        return realEstateDao.getAllRealEstatesByFilters(sold, priceMin, priceMax, surfaceMin, surfaceMax, rooms, bathRooms, bedRooms, agentId);
    }

    public int isRealEstateBelongToUser(long userId, long realEstateId) {
        return realEstateDao.countRealEstateBelongToUser(userId, realEstateId);
    }

}
