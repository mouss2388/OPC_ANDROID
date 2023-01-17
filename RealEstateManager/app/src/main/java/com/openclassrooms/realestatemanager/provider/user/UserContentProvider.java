package com.openclassrooms.realestatemanager.provider.user;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.openclassrooms.realestatemanager.database.AppDatabase;
import com.openclassrooms.realestatemanager.database.model.User;

public class UserContentProvider extends ContentProvider {

    public static final String AUTHORITY = "com.openclassrooms.realestatemanager.provider.user";

    public static final String TABLE_NAME = User.class.getSimpleName();

    public static final Uri URI_ITEM = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        if (getContext() != null) {

            long userId = ContentUris.parseId(uri);

            final Cursor cursor = AppDatabase.getInstance(getContext()).userDao().getUserWithCursor(userId);

            cursor.setNotificationUri(getContext().getContentResolver(), uri);

            return cursor;

        }

        throw new IllegalArgumentException("Failed to query row for uri " + uri);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return "vnd.android.cursor.User/" + AUTHORITY + "." + TABLE_NAME;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        if (getContext() != null && contentValues != null) {

            final long id = AppDatabase.getInstance(getContext()).userDao().insert(User.fromContentValues(contentValues));

            if (id != 0) {

                getContext().getContentResolver().notifyChange(uri, null);

                return ContentUris.withAppendedId(uri, id);

            }

        }

        throw new IllegalArgumentException("Failed to insert row into " + uri);    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        if (getContext() != null) {

            final int count = AppDatabase.getInstance(getContext()).userDao().delete(ContentUris.parseId(uri));

            getContext().getContentResolver().notifyChange(uri, null);

            return count;

        }

        throw new IllegalArgumentException("Failed to delete row into " + uri);    }

    @Override

    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {

        if (getContext() != null && contentValues != null) {

            final int count = AppDatabase.getInstance(getContext()).userDao().update(User.fromContentValues(contentValues));

            getContext().getContentResolver().notifyChange(uri, null);

            return count;

        }

        throw new IllegalArgumentException("Failed to update row into " + uri);
    }
}
