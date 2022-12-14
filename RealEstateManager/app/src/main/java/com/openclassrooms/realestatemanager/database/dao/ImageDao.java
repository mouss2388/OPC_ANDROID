package com.openclassrooms.realestatemanager.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.openclassrooms.realestatemanager.database.model.Image;

import java.util.List;

@Dao
public interface ImageDao {

    @Query("SELECT * FROM image_table WHERE realEstateId =:idRealEstate")
    LiveData<List<Image>> getRealEstatesImages(long idRealEstate);

}
