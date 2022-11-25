package com.openclassrooms.realestatemanager.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.openclassrooms.realestatemanager.database.dao.RealEstateDao;
import com.openclassrooms.realestatemanager.database.dao.UserDao;
import com.openclassrooms.realestatemanager.database.enumeration.TypeRealEstate;
import com.openclassrooms.realestatemanager.database.model.RealEstate;
import com.openclassrooms.realestatemanager.database.model.User;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.Executors;

@Database(entities = {RealEstate.class, User.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "MyDatabase.db";

    // --- SINGLETON ---

    private static volatile AppDatabase INSTANCE;

    // --- DAO ---

    public abstract UserDao userDao();

    public abstract RealEstateDao realEstateDao();


    // --- INSTANCE ---

    public static AppDatabase getInstance(Context context) {

        if (INSTANCE == null) {

            synchronized (AppDatabase.class) {

                if (INSTANCE == null) {

                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),

                                    AppDatabase.class, DB_NAME)
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .addCallback(prepopulateDatabase())
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static Callback prepopulateDatabase() {

        return new Callback() {

            @Override

            public void onCreate(@NonNull SupportSQLiteDatabase db) {

                super.onCreate(db);

                Executors.newSingleThreadExecutor().execute(() -> {

                    RealEstate realEstate = new RealEstate("Bien 1", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce iaculis erat at nunc bibendum, ac varius tellus feugiat. Praesent mi odio, molestie non vehicula a, dictum et lacus. Quisque pellentesque arcu justo. Quisque malesuada placerat odio, interdum placerat dui. Integer nulla purus, lacinia in commodo non, luctus eget ante. Nunc dignissim auctor nisl, nec porta dui ullamcorper viverra. Quisque elementum nisl id orci interdum, non dignissim odio sodales. Fusce ut malesuada enim, ac gravida mi. Nunc porta nisl at tortor mattis varius.", "13 rue des batignolles 75000 Paris", 485321, TypeRealEstate.Flat, 65, 6, 2, false, Utils.getTodayDate());

                    INSTANCE.realEstateDao().insertAll(
                            Arrays.asList(
                                    new RealEstate("Bien 1", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce iaculis erat at nunc bibendum, ac varius tellus feugiat. Praesent mi odio, molestie non vehicula a, dictum et lacus. Quisque pellentesque arcu justo. Quisque malesuada placerat odio, interdum placerat dui. Integer nulla purus, lacinia in commodo non, luctus eget ante. Nunc dignissim auctor nisl, nec porta dui ullamcorper viverra. Quisque elementum nisl id orci interdum, non dignissim odio sodales. Fusce ut malesuada enim, ac gravida mi. Nunc porta nisl at tortor mattis varius.", "13 rue des batignolles 75000 Paris", 123_000, TypeRealEstate.Penthouse, 65, 6, 2, false, Utils.getTodayDate()),

                                    new RealEstate("Bien 2", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce iaculis erat at nunc bibendum, ac varius tellus feugiat. Praesent mi odio, molestie non vehicula a, dictum et lacus. Quisque pellentesque arcu justo. Quisque malesuada placerat odio, interdum placerat dui. Integer nulla purus, lacinia in commodo non, luctus eget ante. Nunc dignissim auctor nisl, nec porta dui ullamcorper viverra. Quisque elementum nisl id orci interdum, non dignissim odio sodales. Fusce ut malesuada enim, ac gravida mi. Nunc porta nisl at tortor mattis varius.", "13 rue des batignolles 75000 Paris", 485_321, TypeRealEstate.Flat, 65, 6, 2, false, Utils.getTodayDate()),

                                            new RealEstate("Bien 3", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce iaculis erat at nunc bibendum, ac varius tellus feugiat. Praesent mi odio, molestie non vehicula a, dictum et lacus. Quisque pellentesque arcu justo. Quisque malesuada placerat odio, interdum placerat dui. Integer nulla purus, lacinia in commodo non, luctus eget ante. Nunc dignissim auctor nisl, nec porta dui ullamcorper viverra. Quisque elementum nisl id orci interdum, non dignissim odio sodales. Fusce ut malesuada enim, ac gravida mi. Nunc porta nisl at tortor mattis varius.", "13 rue des batignolles 75000 Paris", 1_000_000, TypeRealEstate.Manoir, 65, 6, 2, false, Utils.getTodayDate())
                            ));
                });

            }
        };
    }
}
