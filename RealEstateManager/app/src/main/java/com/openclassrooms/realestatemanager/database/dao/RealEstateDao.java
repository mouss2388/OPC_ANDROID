package com.openclassrooms.realestatemanager.database.dao;

import android.database.Cursor;

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


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(RealEstate property);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<RealEstate> realEstates);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int update(RealEstate realEstate);

    @Query("SELECT * FROM realEstate_table WHERE id = :id")
    LiveData<RealEstate> getRealEstateById(long id);

    @Query("SELECT COUNT(*) FROM realEstate_table WHERE agentId = :agentId AND id = :id")
    int countRealEstateBelongToUser(long agentId, long id);

    @Query("SELECT * FROM realEstate_table WHERE agentId = :userId")
    LiveData<List<RealEstate>> getRealEstateByUserId(long userId);

    @Query("SELECT * FROM realEstate_table")
    LiveData<List<RealEstate>> getAllRealEstates();

    @Query("SELECT * FROM realEstate_table WHERE id = :id")
    Cursor getRealEstateWithCursor(long id);

    @Query("SELECT * FROM realEstate_table " +
            "WHERE sold = :sold AND " +
            "(:priceMin IS NULL OR :priceMax IS NULL OR price BETWEEN :priceMin AND :priceMax) AND " +
            "(:surfaceMin IS NULL OR :surfaceMax IS NULL OR surface BETWEEN :surfaceMin AND :surfaceMax) AND" +
            "(:rooms IS NULL OR nbRoom = :rooms ) AND " +
            "(:bathRooms IS NULL OR nbBathRoom = :bathRooms ) AND" +
            "(:bedRooms IS NULL OR nbBedRoom = :bedRooms ) AND" +
            "(:agentId IS NULL OR agentId = :agentId )")
    LiveData<List<RealEstate>> getAllRealEstatesByFilters(boolean sold, Float priceMin, Float priceMax, Float surfaceMin, Float surfaceMax, Integer rooms, Integer bathRooms, Integer bedRooms, Long agentId);
}
