package com.cleanup.todoc.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cleanup.todoc.database.model.Project;

import java.util.List;

@Dao
public interface ProjectDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Project project);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Project> projects);


    @Query("SELECT * FROM project_table")
    LiveData<List<Project>> getAllProjects();


    @Query("SELECT * FROM project_table WHERE id = :id")
    LiveData<Project> getProjectById(long id);

}
