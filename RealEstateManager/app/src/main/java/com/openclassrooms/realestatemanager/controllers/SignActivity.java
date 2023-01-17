package com.openclassrooms.realestatemanager.controllers;


import static com.openclassrooms.realestatemanager.utils.Utils.DEVELOPMENT_MODE;
import static com.openclassrooms.realestatemanager.utils.Utils.EMAIL;
import static com.openclassrooms.realestatemanager.utils.Utils.ERROR_GET_BUNDLE;
import static com.openclassrooms.realestatemanager.utils.Utils.FIRSTNAME;
import static com.openclassrooms.realestatemanager.utils.Utils.LASTNAME;
import static com.openclassrooms.realestatemanager.utils.Utils.PASSWORD;
import static com.openclassrooms.realestatemanager.utils.Utils.PASSWORD_CONFIRM;
import static com.openclassrooms.realestatemanager.utils.Utils.SIGN_CHOICE;
import static com.openclassrooms.realestatemanager.utils.Utils.SIGN_IN;
import static com.openclassrooms.realestatemanager.utils.Utils.SIGN_UP;
import static com.openclassrooms.realestatemanager.utils.Utils.USER_LOGGED_FORMAT_JSON;
import static com.openclassrooms.realestatemanager.utils.Utils.clearErrorOnField;
import static com.openclassrooms.realestatemanager.utils.Utils.setErrorOnField;
import static com.openclassrooms.realestatemanager.utils.Utils.showSnackBar;
import static com.openclassrooms.realestatemanager.utils.Utils.showToast;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.database.model.User;
import com.openclassrooms.realestatemanager.databinding.ActivitySignBinding;
import com.openclassrooms.realestatemanager.viewModel.UserViewModel;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignActivity extends AppCompatActivity {

    private ActivitySignBinding binding;

    private String signId;
    private UserViewModel userViewModel;

    private final Map<String, String> fields = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        signId = getSignId();

        if (signId != null) {
            this.initViewModel();
            this.updateLayout();

        } else {
            showToast(this, ERROR_GET_BUNDLE);
        }

    }

    private String getSignId() {
        Bundle bundle = getIntent().getExtras();
        return bundle.getString(SIGN_CHOICE);
    }

    private void initViewModel() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }


    private void updateLayout() {

        if (signId.equals(SIGN_UP)) {
            updateLayoutToSignUp();
            setupSignUpListener();

        } else {
            updateLayoutToSignIn();
            setupSignInListener();
            if (DEVELOPMENT_MODE) {
                FillFieldsByDefault();
            }
        }
    }


    private void FillFieldsByDefault() {

        userViewModel.getUsersForPrepopulateDB().observe(this, users -> {
            if (users.size() > 0) {
                Objects.requireNonNull(binding.txtFieldEmail.getEditText()).setText(users.get(0).getEmail());
                Objects.requireNonNull(binding.txtFieldPsswrd.getEditText()).setText(users.get(0).getPassword());
            }
        });
    }

    private void updateLayoutToSignUp() {
        binding.notice.setText(this.getResources().getString(R.string.instructionSignUp));
        binding.btnSign.setText(this.getResources().getString(R.string.signUp));
        binding.txtFieldFirstname.setVisibility(View.VISIBLE);
        binding.txtFieldLastname.setVisibility(View.VISIBLE);
        binding.txtFieldEmail.setVisibility(View.VISIBLE);
        binding.txtFieldPsswrd.setVisibility(View.VISIBLE);
        binding.txtFieldPsswrdConfirm.setVisibility(View.VISIBLE);
    }

    private void setupSignUpListener() {

        binding.btnSign.setOnClickListener(v -> {

            setArrayFieldWithTxtFields();

            if (checkFieldsNotEmpties() && !isUserEmailExistAlready() && emailValid() && passwordsAreIdentical()) {

                User user = new User();
                user.setPicture(null);
                user.setFirstname(Objects.requireNonNull(fields.get(FIRSTNAME)));
                user.setLastname(Objects.requireNonNull(fields.get(LASTNAME)));
                user.setEmail(Objects.requireNonNull(fields.get(EMAIL)));
                user.setPassword(Objects.requireNonNull(fields.get(PASSWORD)));

                addUser(user);
            }
        });

    }

    private void setArrayFieldWithTxtFields() {

        if (signId.equals(SIGN_IN)) {
            fields.put(EMAIL, Objects.requireNonNull(binding.txtFieldEmail.getEditText()).getText().toString().trim());
            fields.put(PASSWORD, Objects.requireNonNull(binding.txtFieldPsswrd.getEditText()).getText().toString().trim());
        } else {

            fields.put(FIRSTNAME, Objects.requireNonNull(binding.txtFieldFirstname.getEditText()).getText().toString().trim());
            fields.put(LASTNAME, Objects.requireNonNull(binding.txtFieldLastname.getEditText()).getText().toString().trim());
            fields.put(EMAIL, Objects.requireNonNull(binding.txtFieldEmail.getEditText()).getText().toString().trim());
            fields.put(PASSWORD, Objects.requireNonNull(binding.txtFieldPsswrd.getEditText()).getText().toString().trim());
            fields.put(PASSWORD_CONFIRM, Objects.requireNonNull(binding.txtFieldPsswrdConfirm.getEditText()).getText().toString().trim());
        }
    }
    //TODO USE

    private boolean checkFieldsNotEmpties() {

        if (signId.equals(SIGN_UP)) {
            for (String key : fields.keySet()) {

                if (Objects.requireNonNull(fields.get(key)).isEmpty()) {
                    setErrorOnField(binding,key, getResources().getString(R.string.field_is_requiered));
                    return false;
                } else {
                   clearErrorOnField(binding,key);
                }
            }
        } else {
            if (Objects.requireNonNull(fields.get(EMAIL)).isEmpty()) {
                setErrorOnField(binding,EMAIL, getResources().getString(R.string.field_is_requiered));
                return false;
            } else {
                clearErrorOnField(binding,EMAIL);
            }
            if (Objects.requireNonNull(fields.get(PASSWORD)).isEmpty()) {
                setErrorOnField(binding,PASSWORD, getResources().getString(R.string.field_is_requiered));
                return false;
            } else {
                clearErrorOnField(binding,PASSWORD);
            }

        }

        return true;
    }

    private boolean isUserEmailExistAlready() {
        boolean isUserEmailExistYet = userViewModel.isUserEmailExistAlready(fields.get(EMAIL));
        if (isUserEmailExistYet) {
            setErrorOnField(binding,EMAIL, getResources().getString(R.string.email_exists_already));
        }
        return isUserEmailExistYet;
    }

    private boolean emailValid() {

        boolean emailFormatValid = Patterns.EMAIL_ADDRESS.matcher(Objects.requireNonNull(fields.get(EMAIL))).matches();
        if (!emailFormatValid) {
            setErrorOnField(binding,EMAIL, getResources().getString(R.string.email_invalid));
        }
        return emailFormatValid;
    }

    private boolean passwordsAreIdentical() {

        boolean passwordIdentical = Objects.equals(fields.get(PASSWORD), fields.get(PASSWORD_CONFIRM));

        if (!passwordIdentical) {
            setErrorOnField(binding,PASSWORD_CONFIRM, getResources().getString(R.string.pasword_must_be_identical));
        }
        return passwordIdentical;
    }

    private void addUser(@NonNull User user) {
        long id = userViewModel.insert(user);
        userViewModel.getUserById(id).observe(this, newUser ->{
            startMainActivity(newUser);
            showSnackBar(binding.signActivity, getResources().getString(R.string.sign_up_successfull));

        } );
    }

    private void startMainActivity(User user) {
        Intent intent = new Intent(this, MainActivity.class);
        String userLoggedFormatJson = getUserInJsonFormat(user);
        intent.putExtra(USER_LOGGED_FORMAT_JSON, userLoggedFormatJson);
        intent.putExtra(SIGN_CHOICE,signId);
        finish();
        startActivity(intent);
    }

    private void updateLayoutToSignIn() {
        binding.notice.setText(this.getResources().getString(R.string.instructionSignIn));
        binding.btnSign.setText(this.getResources().getString(R.string.signIn));
        binding.txtFieldFirstname.setVisibility(View.INVISIBLE);
        binding.txtFieldLastname.setVisibility(View.INVISIBLE);
        binding.txtFieldEmail.setVisibility(View.VISIBLE);
        binding.txtFieldPsswrd.setVisibility(View.VISIBLE);
        binding.txtFieldPsswrdConfirm.setVisibility(View.INVISIBLE);
    }

    private void setupSignInListener() {

        binding.btnSign.setOnClickListener(v -> {

            setArrayFieldWithTxtFields();
            if (checkFieldsNotEmpties()) {

                User user = new User();

                user.setEmail(Objects.requireNonNull(fields.get(EMAIL)));
                user.setPassword(Objects.requireNonNull(fields.get(PASSWORD)));


                boolean userRecognized = userViewModel.checkIfPasswordIsCorrect(user);
                if (userRecognized) {
                    user = userViewModel.getUserByEmail(user.getEmail());
                    startMainActivity(user);

                    clearErrorOnField(binding,PASSWORD);
                } else {
                    setErrorOnField(binding,PASSWORD, getResources().getString(R.string.error_email_or_password));
                }
            }
        });
    }

    private String getUserInJsonFormat(User user) {
        return new Gson().toJson(user);
    }
}
