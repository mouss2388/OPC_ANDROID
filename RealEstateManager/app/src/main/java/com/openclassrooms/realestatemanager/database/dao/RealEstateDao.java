package com.openclassrooms.realestatemanager.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.openclassrooms.realestatemanager.database.model.RealEstate;

import java.util.List;

@Dao
public interface RealEstateDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(RealEstate property);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<RealEstate> realEstates);

    @Update
    int update(RealEstate realEstate);

    @Query("SELECT * FROM realEstate_table WHERE id = :id")
    LiveData<RealEstate> getRealEstateById(long id);

    @Query("SELECT * FROM realEstate_table")
    LiveData<List<RealEstate>> getAllRealEstates();



}
