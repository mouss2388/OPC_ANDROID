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
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.openclassrooms.realestatemanager.database.AppDatabase;
import com.openclassrooms.realestatemanager.provider.UserContentProvider;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class UserContentProviderTest {

    // FOR DATA

    private ContentResolver mContentResolver;

    // DATA SET FOR TEST

    private static final long USER_ID = 1;
    private static final long USER_ID_ADDED = 2;

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
    public void getItemsWhenOneItemInserted() {

        final Cursor cursor = mContentResolver.query(ContentUris.withAppendedId(UserContentProvider.URI_ITEM, USER_ID), null, null, null, null);

        assertThat(cursor, notNullValue());

        assertThat(cursor.getCount(), is(1));

        cursor.close();

    }

    @Test
    public void insertAndGetItem() {

        // BEFORE : Adding demo item

        Uri userUri = mContentResolver.insert(UserContentProvider.URI_ITEM, generateUser());

        // TEST

        final Cursor cursor = mContentResolver.query(ContentUris.withAppendedId(UserContentProvider.URI_ITEM, USER_ID_ADDED), null, null, null, null);

        assertThat(cursor, notNullValue());

        assertThat(cursor.getCount(), is(1));

        assertThat(cursor.moveToLast(), is(true));

        assertThat(cursor.getString(cursor.getColumnIndexOrThrow("firstname")), is("firstname_test"));
    }

    // ---

    private ContentValues generateUser() {

        final ContentValues values = new ContentValues();

        values.put("firstname", "firstname_test");

        values.put("lastname", "lastname_test");

        values.put("email", "test@test.com");

        values.put("password", "123");

        values.put("id", 2);

        return values;

    }

}