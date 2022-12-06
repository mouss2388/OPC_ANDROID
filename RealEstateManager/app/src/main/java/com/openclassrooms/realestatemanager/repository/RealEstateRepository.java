package com.openclassrooms.realestatemanager.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.database.AppDatabase;
import com.openclassrooms.realestatemanager.database.dao.RealEstateDao;
import com.openclassrooms.realestatemanager.database.model.RealEstate;

import java.util.List;

public class RealEstateRepository {

    private final RealEstateDao realEstateDao;
    private final LiveData<List<RealEstate>> allRealEstates;

    public RealEstateRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        realEstateDao = db.realEstateDao();
        allRealEstates = realEstateDao.getAllRealEstates();
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
        return allRealEstates;
    }

    public LiveData<List<RealEstate>> getRealEstateByUserId(long id) {
        return realEstateDao.getRealEstateByUserId(id);
    }

}
