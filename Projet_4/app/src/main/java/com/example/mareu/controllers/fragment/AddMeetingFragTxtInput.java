package com.example.mareu.controllers.fragment;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mareu.R;
import com.example.mareu.databinding.FragmentAddMeetingBinding;

import java.util.Objects;

public class AddMeetingFragTxtInput extends Fragment {

    public FragmentAddMeetingBinding binding;
    public static boolean isSubjectValid, isEmailsValid;


    public AddMeetingFragTxtInput() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddMeetingBinding.inflate(inflater, container, false);
        isSubjectValid = false;
        isEmailsValid = false;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initColorForPicker();
        checkInputTexts();
    }

    private void checkInputTexts() {
        isEmailsValid();
        isSubjectValid();
    }


    private void initColorForPicker() {
        binding.colorPickerButton.setBackgroundTintList(ColorStateList.valueOf(-111111));
    }


    public void isSubjectValid() {
        Objects.requireNonNull(binding.textFieldSubject.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isSubjectValid = s.toString().length() >= 5;

                if (isSubjectValid) {
                    binding.textFieldSubject.setErrorEnabled(false);
                } else {
                    binding.textFieldSubject.setErrorEnabled(true);
                    binding.textFieldSubject.setError("Veuillez enter un sujet d'au moins 5 caractères ");
                }
            }
        });
    }

    public void isEmailsValid() {
        String emailPattern = getString(R.string.regex_email);
        Objects.requireNonNull(binding.textFieldListEmails.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String[] emails = s.toString().split("\n");

                for (String email : emails) {
                    if (!email.matches(emailPattern)) {
                        isEmailsValid = false;
                        binding.textFieldListEmails.setError("Veuillez entrer un email");
                        break;
                    } else {
                        isEmailsValid = true;
                        binding.textFieldListEmails.setErrorEnabled(false);
                    }
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}