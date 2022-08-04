package com.example.projet_7.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.projet_7.model.User;
import com.example.projet_7.repository.UserRepository;

import java.util.ArrayList;

public class WorkMateViewModel extends ViewModel {

    private final UserRepository userRepository;


    public WorkMateViewModel() {
        userRepository = new UserRepository();
    }

    public LiveData<ArrayList<User>> getLiveData() {
        return userRepository.getMutableLiveData();
    }

    public void getWorkMates() {
        userRepository.getUsersData();
    }


}
