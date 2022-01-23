package com.example.mareu.controllers.fragment;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mareu.databinding.FragmentAddMeetingBinding;
import com.google.android.material.timepicker.MaterialTimePicker;

import java.util.Arrays;
import java.util.Objects;

public class AddMeetingFragment extends Fragment {

    public FragmentAddMeetingBinding mBinding;
    public static boolean isSubjectValid, isEmailsValid;


    public AddMeetingFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentAddMeetingBinding.inflate(inflater, container, false);
        isSubjectValid = false;
        isEmailsValid = false;
        return mBinding.getRoot();
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
        mBinding.colorPickerButton.setBackgroundTintList(ColorStateList.valueOf(-111111));
    }


    public void isSubjectValid() {
        Objects.requireNonNull(mBinding.textFieldSubject.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isSubjectValid = s.toString().length() >= 5;
                Log.i("TAG Subject", s.toString());
                Log.i("TAG Subj isSubjValid[0]", String.valueOf(isSubjectValid));

                if (isSubjectValid) {
                    mBinding.textFieldSubject.setErrorEnabled(false);
                } else {
                    mBinding.textFieldSubject.setErrorEnabled(true);
                    mBinding.textFieldSubject.setError("Veuillez enter un sujet d'au moins 5 caractères ");
                }
            }
        });
    }

    public void isEmailsValid() {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        Objects.requireNonNull(mBinding.textFieldListEmails.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String[] emails = s.toString().split("\n");
                Log.i("TAG isEmailValid", Arrays.toString(emails));

                for (String email : emails) {
                    if (!email.matches(emailPattern)) {
                        isEmailsValid = false;
                        mBinding.textFieldListEmails.setError("Veuillez entrer un email");
                        break;
                    } else {
                        isEmailsValid = true;
                        mBinding.textFieldListEmails.setErrorEnabled(false);
                    }
                }
                Log.i("TAG m isEmailValid[0]", String.valueOf(isEmailsValid));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}