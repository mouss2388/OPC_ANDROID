package com.cleanup.todoc.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.cleanup.todoc.controller.activity.MainActivity.SortMethod;
import com.cleanup.todoc.database.AppDatabase;
import com.cleanup.todoc.database.dao.TaskDao;
import com.cleanup.todoc.database.model.Task;

import java.util.List;

public class TaskRepository {

    private final TaskDao taskDao;
    private final LiveData<List<Task>> allTasks;

    public TaskRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        taskDao = db.taskDao();
        allTasks = taskDao.getAllTasks();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public long insert(Task task) {
        return taskDao.insert(task);
    }

    public void delete(Task task) {
        taskDao.delete(task.getId());
    }

    public LiveData<List<Task>> getAllTaskSort(SortMethod sortMethod) {
        switch (sortMethod) {

            case ALPHABETICAL:
                return taskDao.getAllTaskAZComparator();

            case ALPHABETICAL_INVERTED:
                return taskDao.getAllTaskZAComparator();

            case RECENT_FIRST:
                return taskDao.getAllTaskRecentComparator();

            default:
                return taskDao.getAllTaskOldComparator();
        }
    }
}
