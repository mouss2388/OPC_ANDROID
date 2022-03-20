package com.cleanup.todoc.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cleanup.todoc.controller.activity.MainActivity.SortMethod;
import com.cleanup.todoc.database.model.Task;
import com.cleanup.todoc.repository.TaskRepository;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    private final TaskRepository repository;

    private final LiveData<List<Task>> allTasks;

    public TaskViewModel(Application application) {
        super(application);
        repository = new TaskRepository(application);
        allTasks = repository.getAllTasks();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public long insert(Task task) {
        return repository.insert(task);
    }

    public int delete(Task task) {
        return repository.delete(task);
    }

    public LiveData<List<Task>> getAllTaskSort(SortMethod sortMethod) {
        switch (sortMethod) {
            case ALPHABETICAL:
                return repository.getAllTaskSort(SortMethod.ALPHABETICAL);

            case ALPHABETICAL_INVERTED:
                return repository.getAllTaskSort(SortMethod.ALPHABETICAL_INVERTED);

            case RECENT_FIRST:
                return repository.getAllTaskSort(SortMethod.RECENT_FIRST);

            default:
                return repository.getAllTaskSort(SortMethod.OLD_FIRST);
        }
    }


}