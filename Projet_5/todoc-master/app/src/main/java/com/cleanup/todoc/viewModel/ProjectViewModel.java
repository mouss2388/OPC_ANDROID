package com.cleanup.todoc.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cleanup.todoc.database.model.Project;
import com.cleanup.todoc.repository.ProjectRepository;

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

    public long insert(Project project) {
        return repository.insert(project);
    }

    public LiveData<Project> getProjectById(long id) {
        return repository.getProjectById(id);
    }

}