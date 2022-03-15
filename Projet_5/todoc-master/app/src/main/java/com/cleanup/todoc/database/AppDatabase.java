package com.cleanup.todoc.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.cleanup.todoc.database.dao.ProjectDao;
import com.cleanup.todoc.database.dao.TaskDao;
import com.cleanup.todoc.database.model.Project;
import com.cleanup.todoc.database.model.Task;

import java.util.Arrays;
import java.util.concurrent.Executors;

@Database(entities = {Task.class, Project.class}, version = 1, exportSchema = false)

public abstract class AppDatabase extends RoomDatabase {

    // --- SINGLETON ---

    private static volatile AppDatabase INSTANCE;

    // --- DAO ---

    public abstract ProjectDao projectDao();

    public abstract TaskDao taskDao();

    // --- INSTANCE ---

    public static AppDatabase getInstance(Context context) {

        if (INSTANCE == null) {

            synchronized (AppDatabase.class) {

                if (INSTANCE == null) {

                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),

                            AppDatabase.class, "MyDatabase.db")
                            .allowMainThreadQueries()
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
//                    INSTANCE.projectDao().insert(
//                            new Project(1L, "Projet Tartampion", 0xFFEADAD1));
//                    INSTANCE.projectDao().insert(new Project(2L, "Projet Lucidia", 0xFFB4CDBA));
//                    INSTANCE.projectDao().insert(new Project(3L, "Projet Circus", 0xFFA3CED2));
//                }
                    INSTANCE.projectDao().insertAll(
                            Arrays.asList(
                                    new Project("Projet Tartampion", 0xFFEADAD1),
                                    new Project("Projet Lucidia", 0xFFB4CDBA),
                                    new Project("Projet Circus", 0xFFA3CED2)));

                });

            }

        };

    }
}
