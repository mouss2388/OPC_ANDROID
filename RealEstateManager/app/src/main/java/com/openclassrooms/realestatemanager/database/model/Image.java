package com.openclassrooms.realestatemanager.database.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "image_table",
        foreignKeys = @ForeignKey(entity = RealEstate.class,
                parentColumns = "id",
                childColumns = "realEstateId"), indices = {
        @Index("realEstateId")
})
public class Image {
    /**
     * The unique identifier of the property
     */
    @PrimaryKey(autoGenerate = true)
    public long id;

    @Nullable
    public Long realEstateId;

    @NonNull
    public String url;

    @Ignore
    public Image() {
    }

    public Image(@Nullable Long realEstateId, @NonNull String url) {
        this.realEstateId = realEstateId;
        this.url = url;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Nullable
    public Long getRealEstateId() {
        return realEstateId;
    }

    public void setRealEstateId(@Nullable Long realEstateId) {
        this.realEstateId = realEstateId;
    }

    @NonNull
    public String getUrl() {
        return url;
    }

    public void setUrl(@NonNull String url) {
        this.url = url;
    }
}
