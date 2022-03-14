package com.cleanup.todoc.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cleanup.todoc.database.model.Task;

@Dao
public interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Task task);


    @Query("DELETE  FROM task_table WHERE id = :id")
    void delete(long id);
}
