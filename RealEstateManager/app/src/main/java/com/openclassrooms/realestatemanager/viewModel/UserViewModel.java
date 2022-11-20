package com.openclassrooms.realestatemanager.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.openclassrooms.realestatemanager.database.model.User;
import com.openclassrooms.realestatemanager.repository.UserRepository;

public class UserViewModel extends AndroidViewModel {

    private final UserRepository repository;

    public UserViewModel(Application application) {
        super(application);
        repository = new UserRepository(application);

    }

    public long insert(User user) {
        return repository.insert(user);
    }


    public boolean checkIfEmailExistYet(String email) {
        return repository.checkIfEmailExistYet(email);
    }

    public boolean checkIfPasswordIsCorrect(User user) {
        return repository.checkIfPasswordIsCorrect(user);
    }
}