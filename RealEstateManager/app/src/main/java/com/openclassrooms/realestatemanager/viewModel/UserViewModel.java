package com.openclassrooms.realestatemanager.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.openclassrooms.realestatemanager.database.model.User;
import com.openclassrooms.realestatemanager.repository.UserRepository;

import java.util.List;

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

    public User getUserByEmail(String email) {
        return repository.getUserByEmail(email);
    }

    public LiveData<User> getUserById(long id) {
        return repository.getUserById(id);
    }


    public LiveData<List<User>> getUsersForPrepopulateDB() {
        return repository.getUsersForPrepopulateDB();
    }

    public int update(User user){
        return repository.update(user);
    }
}