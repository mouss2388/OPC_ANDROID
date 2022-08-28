package com.example.projet_7.manager;

import android.content.Context;
import android.widget.Toast;

import com.example.projet_7.model.User;
import com.example.projet_7.repository.UserRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Objects;

public class UserManager {

    private static volatile UserManager instance;
    private final UserRepository userRepository;

    private UserManager() {
        userRepository = UserRepository.getInstance();
    }

    public static UserManager getInstance() {
        UserManager result = instance;
        if (result != null) {
            return result;
        }
        synchronized (UserRepository.class) {
            if (instance == null) {
                instance = new UserManager();
            }
            return instance;
        }
    }

    public FirebaseUser getCurrentUser() {
        return userRepository.getCurrentUser();
    }

    public Boolean isCurrentUserLogged() {
        return (this.getCurrentUser() != null);
    }

    public Task<Void> signOut(Context context) {
        return userRepository.signOut(context);
    }

    ////FIRESTORE

    public void createUser() {
        userRepository.createUser();
    }

    public Task<DocumentSnapshot> isUserExists() {
        return userRepository.isUserExists();
    }

    public Task<User> getUserData() {
        // Get the user from Firestore and cast it to a User model Object
        return Objects.requireNonNull(userRepository.getUserData()).continueWith(task -> task.getResult().toObject(User.class));
    }

    public Task<Void> deleteUser(Context context) {
        // Delete the user account from the Auth
        userRepository.deleteUserFromFirestore();
        return userRepository.deleteUser(context).addOnCompleteListener(task -> {
            // Once done, delete the user datas from Firestore
            Toast.makeText(context, "User supprimé", Toast.LENGTH_SHORT).show();
        });
    }

    public void updateUserData(User user) {
        userRepository.updateUser(user);
    }
}