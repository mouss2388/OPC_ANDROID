package com.openclassrooms.realestatemanager.controllers;


import static com.openclassrooms.realestatemanager.utils.Utils.ERROR_GET_BUNDLE;
import static com.openclassrooms.realestatemanager.utils.Utils.SIGN_CHOICE;
import static com.openclassrooms.realestatemanager.utils.Utils.SIGN_IN;
import static com.openclassrooms.realestatemanager.utils.Utils.SIGN_UP;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.database.model.User;
import com.openclassrooms.realestatemanager.databinding.ActivitySignBinding;
import com.openclassrooms.realestatemanager.viewModel.UserViewModel;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignActivity extends AppCompatActivity {

    private final String FIRSTNAME = "FIRSTNAME";
    private final String LASTNAME = "LASTNAME";
    private final String EMAIL = "EMAIL";
    private final String PASSWORD = "PASSWORD";
    private final String PASSWORD_CONFIRM = "PASSWORD_CONFIRM";

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

        Toast.makeText(this, "onCreate", Toast.LENGTH_LONG).show();

        signId = getSignId();

        if (signId != null) {
            this.initViewModel();
            this.updateLayout();

        } else {
            Toast.makeText(this, ERROR_GET_BUNDLE, Toast.LENGTH_LONG).show();
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
        }
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

            if (checkFieldsNotEmpties() && !checkUserExistYet() && emailValid() && passwordsAreIdentical()) {

                User user = new User();
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

    private boolean checkFieldsNotEmpties() {

        if (signId.equals(SIGN_UP)) {
            for (String key : fields.keySet()) {

                if (Objects.requireNonNull(fields.get(key)).isEmpty()) {
                    setErrorOnField(key, "Field Empty");
                    return false;
                } else {
                    clearErrorOnField(key);
                }
            }
        } else {
            if (Objects.requireNonNull(fields.get(PASSWORD)).isEmpty()) {
                setErrorOnField(PASSWORD, "Field Empty");
                return false;
            } else {
                clearErrorOnField(PASSWORD);
            }

            if (Objects.requireNonNull(fields.get(EMAIL)).isEmpty()) {
                setErrorOnField(EMAIL, "Field Empty");
                return false;
            } else {
                clearErrorOnField(EMAIL);
            }
        }

        return true;
    }

    private void setErrorOnField(String key, String message) {
        switch (key) {
            case FIRSTNAME:
                binding.txtFieldFirstname.setError(message);
                return;

            case LASTNAME:
                binding.txtFieldLastname.setError(message);
                return;

            case EMAIL:
                binding.txtFieldEmail.setError(message);
                return;

            case PASSWORD:
                binding.txtFieldPsswrd.setError(message);
                return;

            case PASSWORD_CONFIRM:
                binding.txtFieldPsswrdConfirm.setError(message);
                return;

            default:
                break;
        }
    }

    private void clearErrorOnField(String key) {
        switch (key) {
            case FIRSTNAME:
                binding.txtFieldFirstname.setError(null);
                return;

            case LASTNAME:
                binding.txtFieldLastname.setError(null);
                return;

            case EMAIL:
                binding.txtFieldEmail.setError(null);
                return;

            case PASSWORD:
                binding.txtFieldPsswrd.setError(null);
                return;

            case PASSWORD_CONFIRM:
                binding.txtFieldPsswrdConfirm.setError(null);
                return;

            default:
                break;
        }
    }

    private boolean checkUserExistYet() {
        boolean isUserExistYet = userViewModel.checkIfEmailExistYet(fields.get(EMAIL));
        if (isUserExistYet) {
            setErrorOnField(EMAIL, "Email already used");
        }
        return isUserExistYet;
    }

    private boolean emailValid() {

        boolean emailFormatValid = Patterns.EMAIL_ADDRESS.matcher(Objects.requireNonNull(fields.get(EMAIL))).matches();
        if (!emailFormatValid) {
            setErrorOnField(EMAIL, "Email invalid");
        }
        return emailFormatValid;
    }

    private boolean passwordsAreIdentical() {

        boolean passwordIdentical = Objects.equals(fields.get(PASSWORD), fields.get(PASSWORD_CONFIRM));

        if (!passwordIdentical) {
            setErrorOnField(PASSWORD_CONFIRM, "Passwords different");
        }
        return passwordIdentical;
    }

    private void addUser(@NonNull User user) {
        userViewModel.insert(user);
        startMainActivity();
        Toast.makeText(getApplicationContext(), "Account create", Toast.LENGTH_SHORT).show();
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
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

                //TODO BUG IF NOT MATCH WITH EMAIL RETURN NULL WHICH != BOOLEAN
                boolean userRecognized = userViewModel.checkIfPasswordIsCorrect(user);
                if (userRecognized) {
                    startMainActivity();
                    Toast.makeText(getApplicationContext(), "SIgn In successful", Toast.LENGTH_SHORT).show();

                    clearErrorOnField(PASSWORD);
                } else {
                    setErrorOnField(PASSWORD, "Email or password incorrect");
                }
            }
        });
    }
}
