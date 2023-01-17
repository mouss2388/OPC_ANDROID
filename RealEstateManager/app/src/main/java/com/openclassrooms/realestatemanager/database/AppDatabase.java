package com.openclassrooms.realestatemanager.database;

import static com.openclassrooms.realestatemanager.utils.Utils.DEVELOPMENT_MODE;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.openclassrooms.realestatemanager.database.DI.DI;
import com.openclassrooms.realestatemanager.database.dao.ImageDao;
import com.openclassrooms.realestatemanager.database.dao.RealEstateDao;
import com.openclassrooms.realestatemanager.database.dao.UserDao;
import com.openclassrooms.realestatemanager.database.model.Image;
import com.openclassrooms.realestatemanager.database.model.RealEstate;
import com.openclassrooms.realestatemanager.database.model.User;
import com.openclassrooms.realestatemanager.database.service.image.ImageApiService;
import com.openclassrooms.realestatemanager.database.service.realEstate.RealEstateApiService;
import com.openclassrooms.realestatemanager.database.service.user.UserApiService;

import java.util.concurrent.Executors;

@Database(entities = {RealEstate.class, User.class, Image.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "MyDatabase.db";

    // --- SINGLETON ---

    private static volatile AppDatabase INSTANCE;

    // --- DAO ---

    public abstract UserDao userDao();

    public abstract ImageDao imageDao();

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
        RealEstateApiService realEstateApiService = DI.getRealEstatesApiService();
        UserApiService userApiService = DI.getUserApiService();
        ImageApiService imageApiService = DI.getImageApiService();

        return new Callback() {

            @Override

            public void onCreate(@NonNull SupportSQLiteDatabase db) {

                super.onCreate(db);

                Executors.newSingleThreadExecutor().execute(() -> {

                    if (DEVELOPMENT_MODE) {

                        INSTANCE.userDao().insert(
                                userApiService.getUsers().get(0));

                        INSTANCE.realEstateDao().insertAll(
                                realEstateApiService.getRealEstates());

                        INSTANCE.imageDao().insertAll(imageApiService.getImages());
                    }

                });

            }
        };
    }
}
