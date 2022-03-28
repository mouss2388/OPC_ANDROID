package com.cleanup.todoc.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cleanup.todoc.database.model.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Task task);

    @Query("DELETE  FROM task_table WHERE id = :id")
    void delete(long id);

    @Query("SELECT * FROM task_table ORDER BY id ASC")
    LiveData<List<Task>> getAllTasks();

    @Query("SELECT * FROM task_table WHERE id = :id")
    LiveData<Task> getTaskById(long id);

    @Query("SELECT * FROM task_table ORDER BY name ASC")
    LiveData<List<Task>> getAllTaskAZComparator();

    @Query("SELECT * FROM task_table ORDER BY name DESC")
    LiveData<List<Task>> getAllTaskZAComparator();

    @Query("SELECT * FROM task_table ORDER BY creationTimestamp DESC")
    LiveData<List<Task>> getAllTaskRecentComparator();

    @Query("SELECT * FROM task_table ORDER BY creationTimestamp ASC")
    LiveData<List<Task>> getAllTaskOldComparator();
}
