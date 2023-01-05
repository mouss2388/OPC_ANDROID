package com.openclassrooms.realestatemanager.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.openclassrooms.realestatemanager.database.model.Image;
import com.openclassrooms.realestatemanager.database.model.RealEstate;

import java.util.List;

@Dao
public interface ImageDao {

    @Query("SELECT * FROM image_table WHERE realEstateId =:idRealEstate")
    LiveData<List<Image>> getRealEstateImages(long idRealEstate);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<Image> images);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Image image);

    @Delete
    void deleteAll(List<Image> images);

    @Delete
    int delete(Image image);

    @Query("SELECT COUNT(id) FROM image_table WHERE realEstateId=:idRealEstate")
    int getNumberOfImages(long idRealEstate);
}
