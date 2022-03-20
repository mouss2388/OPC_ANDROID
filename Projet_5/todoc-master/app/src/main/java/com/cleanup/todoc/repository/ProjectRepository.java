package com.cleanup.todoc.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.cleanup.todoc.database.AppDatabase;
import com.cleanup.todoc.database.dao.ProjectDao;
import com.cleanup.todoc.database.model.Project;

import java.util.List;

public class ProjectRepository {

    private final ProjectDao projectDao;
    private final LiveData<List<Project>> allProjects;

    public ProjectRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        projectDao = db.projectDao();
        allProjects = projectDao.getAllProjects();
    }

    public LiveData<List<Project>> getAllProjects() {
        return allProjects;
    }

    public void insertAll(List<Project> projects) {
        projectDao.insertAll(projects);
    }

    public long insert(Project project) {
        return projectDao.insert(project);
    }

    public LiveData<Project> getProjectById(long id) {
        return projectDao.getProjectById(id);
    }

}
