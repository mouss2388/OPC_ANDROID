package com.cleanup.todoc.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cleanup.todoc.database.model.Project;
import com.cleanup.todoc.database.model.Task;
import com.cleanup.todoc.repository.ProjectRepository;
import com.cleanup.todoc.repository.TaskRepository;

import java.util.List;

public class ProjectViewModel extends AndroidViewModel {

    private final ProjectRepository repository;

    private final LiveData<List<Project>> allProjects;

    public ProjectViewModel(Application application) {
        super(application);
        repository = new ProjectRepository(application);
        allProjects = repository.getAllProjects();
    }

    public LiveData<List<Project>> getAllProjects() {
        return allProjects;
    }

    public void insert(Project project) {
        repository.insert(project);
    }

    public void insertAll(List<Project> projects) {
        repository.insertAll(projects);
    }

    public LiveData<Project> getProjectById(long id){return repository.getProjectById(id);}

}