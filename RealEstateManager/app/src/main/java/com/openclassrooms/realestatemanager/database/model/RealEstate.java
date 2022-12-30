package com.openclassrooms.realestatemanager.database.model;


import android.content.ContentValues;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.openclassrooms.realestatemanager.database.enumeration.Currency;
import com.openclassrooms.realestatemanager.database.enumeration.TypeRealEstate;

@Entity(tableName = "realEstate_table",
        foreignKeys = @ForeignKey(entity = User.class,
                parentColumns = "id",
                childColumns = "agentId"))
public class RealEstate {

    /**
     * The unique identifier of the property
     */
    @PrimaryKey(autoGenerate = true)
    private long id;

    @Nullable
    private Long agentId;


    @NonNull
    private String name;

    @NonNull
    private Double price;

    @Nullable
    private Currency currency;

    @NonNull
    private TypeRealEstate typeRealEstate;

    @NonNull
    private Integer surface;

    @NonNull
    private Integer nbRoom;

    @NonNull
    private Integer nbBedRoom;


    @NonNull
    private Integer nbBathRoom;

    @NonNull
    private String description;

    @NonNull
    private String address;

    @NonNull
    private Boolean sold;

    @NonNull
    private String dateOfEntry;

    @Nullable
    private String dateOfSell;


    @NonNull
    private String interestPoint;

    @Ignore
    public RealEstate() {
    }

    public RealEstate(@Nullable Long agentId, @NonNull String name, @NonNull Double price, @NonNull TypeRealEstate typeRealEstate, @NonNull Integer surface, @NonNull Integer nbRoom, @NonNull Integer nbBedRoom, @NonNull Integer nbBathRoom, @NonNull String description, @NonNull String address, @NonNull Boolean sold, @NonNull String dateOfEntry, @NonNull String interestPoint) {
        this.setAgentId(agentId);
        this.setName(name);
        this.setDescription(description);
        this.setAddress(address);
        this.setPrice(price);
        this.setTypeRealEstate(typeRealEstate);
        this.setSurface(surface);
        this.setNbRoom(nbRoom);
        this.setNbBedRoom(nbBedRoom);
        this.setNbBathRoom(nbBathRoom);
        this.setSold(sold);
        this.setDateOfEntry(dateOfEntry);
        this.setCurrency(Currency.dollar);
        this.setInterestPoint(interestPoint);

    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Nullable
    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(@Nullable Long agentId) {
        this.agentId = agentId;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public Double getPrice() {
        return price;
    }

    public void setPrice(@NonNull Double price) {
        this.price = price;
    }

    @NonNull
    public TypeRealEstate getTypeRealEstate() {
        return typeRealEstate;
    }

    public void setTypeRealEstate(@NonNull TypeRealEstate typeRealEstate) {
        this.typeRealEstate = typeRealEstate;
    }

    @NonNull
    public Integer getSurface() {
        return surface;
    }

    public void setSurface(@NonNull Integer surface) {
        this.surface = surface;
    }

    @NonNull
    public Integer getNbRoom() {
        return nbRoom;
    }

    public void setNbRoom(@NonNull Integer nbRoom) {
        this.nbRoom = nbRoom;
    }

    @NonNull
    public Integer getNbBathRoom() {
        return nbBathRoom;
    }

    public void setNbBathRoom(@NonNull Integer nbBathRoom) {
        this.nbBathRoom = nbBathRoom;
    }

    @NonNull
    public Integer getNbBedRoom() {
        return nbBedRoom;
    }

    public void setNbBedRoom(@NonNull Integer nbBedRoom) {
        this.nbBedRoom = nbBedRoom;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    @NonNull
    public String getAddress() {
        return address;
    }

    public void setAddress(@NonNull String address) {
        this.address = address;
    }

    @NonNull
    public Boolean getSold() {
        return sold;
    }

    public void setSold(@NonNull Boolean sold) {
        this.sold = sold;
    }

    @NonNull
    public String getDateOfEntry() {
        return dateOfEntry;
    }


    public void setDateOfEntry(@NonNull String dateOfEntry) {
        this.dateOfEntry = dateOfEntry;
    }

    @Nullable
    public String getDateOfSell() {
        return dateOfSell;
    }

    public void setDateOfSell(@Nullable String dateOfSell) {
        this.dateOfSell = dateOfSell;
    }


    @Nullable
    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(@Nullable Currency currency) {
        this.currency = currency;
    }

    @NonNull
    public String getInterestPoint() {
        return interestPoint;
    }

    public void setInterestPoint(@NonNull String interestPoint) {
        this.interestPoint = interestPoint;
    }

    public static RealEstate fromContentValues(ContentValues values) {

        final RealEstate realEstate = new RealEstate();

        if (values.containsKey("id")) realEstate.setId(values.getAsLong("id"));
        if (values.containsKey("agentId")) realEstate.setAgentId(values.getAsLong("agentId"));
        if (values.containsKey("name")) realEstate.setName(values.getAsString("name"));
        if (values.containsKey("description"))
            realEstate.setDescription(values.getAsString("description"));
        if (values.containsKey("address")) realEstate.setAddress(values.getAsString("address"));
        if (values.containsKey("price")) realEstate.setPrice(values.getAsDouble("price"));
        if (values.containsKey("surface")) realEstate.setSurface(values.getAsInteger("surface"));
        if (values.containsKey("nbRoom")) realEstate.setNbRoom(values.getAsInteger("nbRoom"));
        if (values.containsKey("nbBedRoom"))
            realEstate.setNbBedRoom(values.getAsInteger("nbBedRoom"));
        if (values.containsKey("nbBathRoom"))
            realEstate.setNbBathRoom(values.getAsInteger("nbBathRoom"));
        if (values.containsKey("typeRealEstate"))
            realEstate.setTypeRealEstate(TypeRealEstate.House);
        if (values.containsKey("sold")) realEstate.setSold(values.getAsBoolean("sold"));
        if (values.containsKey("dateOfEntry"))
            realEstate.setDateOfEntry(values.getAsString("dateOfEntry"));
        if (values.containsKey("dateOfSell"))
            realEstate.setDateOfSell(values.getAsString("dateOfSell"));
        if (values.containsKey("currency"))
            realEstate.setCurrency(Currency.dollar);
        if (values.containsKey("interestPoint"))
            realEstate.setInterestPoint("Ecole, magasin");

        return realEstate;
    }

    @Override
    @NonNull
    public String toString() {
        return "RealEstate{" +
                "id=" + id +
                ", agentId=" + agentId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", typeRealEstate=" + typeRealEstate +
                ", dateOfEntry='" + dateOfEntry + '\'' + '}';
    }
}
