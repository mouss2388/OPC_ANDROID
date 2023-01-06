package com.openclassrooms.realestatemanager;

import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;

import com.openclassrooms.realestatemanager.database.AppDatabase;
import com.openclassrooms.realestatemanager.database.DI.DI;
import com.openclassrooms.realestatemanager.database.enumeration.Currency;
import com.openclassrooms.realestatemanager.database.enumeration.TypeRealEstate;
import com.openclassrooms.realestatemanager.database.service.realEstate.RealEstateApiService;
import com.openclassrooms.realestatemanager.provider.realEstate.RealEstateContentProvider;
import com.openclassrooms.realestatemanager.utils.Utils;

import org.junit.Before;
import org.junit.Test;

public class RealEstateContentProviderTest {

    // FOR DATA

    private ContentResolver mContentResolver;

    // DATA SET FOR TEST
    private final static RealEstateApiService realEstateApiService = DI.getNewInstanceRealEstateApiService();

    private static final long REAL_ESTATE_ID_ADDED = realEstateApiService.getRealEstates().size() + 1;

    @Before
    public void setUp() {

        Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getContext()
                        ,

                        AppDatabase.class)


                .allowMainThreadQueries()


                .build();


        mContentResolver = InstrumentationRegistry.getInstrumentation().getContext()

                .getContentResolver();

    }


    @Test
    public void insertAndGetRealEstate() {

        // BEFORE : Adding demo item
        Uri realEstateUri = mContentResolver.insert(RealEstateContentProvider.URI_ITEM, generateRealEstate());

        // TEST
        final Cursor cursor = mContentResolver.query(ContentUris.withAppendedId(RealEstateContentProvider.URI_ITEM, REAL_ESTATE_ID_ADDED), null, null, null, null);

        assertThat(cursor, notNullValue());

        assertThat(cursor.getCount(), is(1));
        assertThat(cursor.moveToLast(), is(true));

        assertThat(cursor.getString(cursor.getColumnIndexOrThrow("name")), is("Bien test"));
    }


    private ContentValues generateRealEstate() {

        final ContentValues values = new ContentValues();

        values.put("agentId", "1");
        values.put("name", "Bien test");
        values.put("description", "Description test");
        values.put("address", "address_test");
        values.put("price", "150000.00");
        values.put("surface", "250");
        values.put("nbRoom", "12");
        values.put("nbBedRoom", "4");
        values.put("nbBathRoom", "5");
        values.put("typeRealEstate", String.valueOf(TypeRealEstate.House));
        values.put("sold", "true");
        values.put("dateOfEntry", Utils.getTodayDate());
        values.put("dateOfSell", Utils.getTodayDate());
        values.put("currency", String.valueOf(Currency.dollar));
        values.put("interestPoint", "Ecole, magasin test");

        return values;
    }
}
