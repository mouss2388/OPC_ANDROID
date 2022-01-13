package com.example.mareu.controllers.fragment;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mareu.DI.DI;
import com.example.mareu.R;
import com.example.mareu.databinding.FragmentAddMeetingBinding;
import com.example.mareu.model.Reunion;
import com.example.mareu.service.ReunionApiService;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class AddMeetingFragment extends Fragment {

    private FragmentAddMeetingBinding mBinding;
    private MaterialTimePicker mMaterialTimePicker;
    private ReunionApiService mReunionApiService = DI.getReunionApiService();


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
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initColorForPicker();
        if (!isTablet()) {
            subjectValid();
            emailValid();

            assert mBinding.btnTimePicker != null;
            mBinding.btnTimePicker.setOnClickListener(v ->
                    showTimePicker());
            assert mBinding.btnAdd != null;
            mBinding.btnAdd.setOnClickListener(v ->
                    onSubmit());
        }
    }



    private void initColorForPicker() {
        mBinding.colorPickerButton.setBackgroundTintList(ColorStateList.valueOf(-111111));
    }

    private void showTimePicker() {
        int timeMode = getHeightDevice() < 720 ? MaterialTimePicker.INPUT_MODE_KEYBOARD : MaterialTimePicker.INPUT_MODE_CLOCK;
        MaterialTimePicker picker = new
                MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setInputMode(timeMode)
                .setTitleText(R.string.hour_meeting)
                .setHour(12)
                .setMinute(0)
                .build();


        picker.show(getParentFragmentManager(), "MaterialTimePicker");
        manageStateTimePicker(picker);
    }

    private int getHeightDevice() {
        //GET DIMENSION DEVICE
        DisplayMetrics metrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }

    private void manageStateTimePicker(MaterialTimePicker picker) {
        picker.addOnPositiveButtonClickListener(v -> {

                    StringBuilder hourMeeting = new StringBuilder();
                    hourMeeting.append(picker.getHour())
                            .append(":")
                            .append(picker.getMinute());
                    if (picker.getMinute() == 0) {
                        hourMeeting.append('0');
                    }
                    Log.i("click", "addOnPositiveButtonClickListener");
                    assert mBinding.btnTimePicker != null;
                    mBinding.btnTimePicker.setText(hourMeeting);
                    mMaterialTimePicker = picker;
                    timePickerSetStatus(true);
                }
        );

        picker.addOnNegativeButtonClickListener(v -> Log.i("click", "addOnNegativeButtonClickListener"));
        picker.addOnCancelListener(dialog -> Log.i("click", "addOnCancelListener"));
        picker.addOnDismissListener(dialog -> Log.i("click", "addOnDismissListener"));
    }

    private boolean emailValid() {
        final boolean[] isValid = {true};
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        mBinding.textFieldListEmails.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String[] emails = s.toString().split("\n");
                Log.i("TAG", emails.toString());
                for (String email : emails) {
                    if (!email.matches(emailPattern)) {
                        isValid[0] = false;
                        mBinding.textFieldListEmails.setError("Veuillez entrer un email");
                        break;
                    } else {
                        isValid[0] = true;
                        mBinding.textFieldListEmails.setErrorEnabled(false);
                    }
                }
            }
        });
        return isValid[0];
    }

    private void subjectValid() {
        mBinding.textFieldSubject.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length() >= 5) {
                    mBinding.textFieldSubject.setErrorEnabled(false);
                }else{
                    mBinding.textFieldSubject.setError("Veuillez enter un sujet d'au moins 5 caractères ");

                }
            }
        });
    }


    private void onSubmit() {

        int color = Objects.requireNonNull(mBinding.colorPickerButton.getBackgroundTintList()).getDefaultColor();
        String subject = mBinding.textFieldSubject.getEditText().getText().toString();


        String[] emailList = mBinding.textFieldListEmails.getEditText().getText().toString().split("\n");
        String room = mBinding.spinnerRoom.getSelectedItem().toString();
        Date hourly = new Date();
        int hour, minute;
        ArrayList<String> emails = new ArrayList<>();

        if (subject.isEmpty()) {
            mBinding.textFieldSubject.setError("Veuillez enter un sujet ");
            return;
        } else {
            mBinding.textFieldSubject.setErrorEnabled(false);
        }

        if (emailList[0].equals("")) {
            mBinding.textFieldListEmails.setError("Veuillez entrer un email");
            return;
        } else {
            if (!emailValid()) {
                return;
            }
            mBinding.textFieldListEmails.setErrorEnabled(false);
        }

        if (mMaterialTimePicker == null) {
            Toast.makeText(getContext(), "Veuillez entrer un horaire", Toast.LENGTH_SHORT).show();
            timePickerSetStatus(false);
            return;
        }
        hour = mMaterialTimePicker.getHour();
        minute = mMaterialTimePicker.getMinute();

        Log.d("TAG color", String.valueOf(color));
        Log.d("TAG subject", subject);
        Log.d("TAG room", room);
        emails.addAll(Arrays.asList(emailList));

        hourly.setTime(convertTimeToMillis(hour, minute));
        Log.d("TAG", convertMillisToStr(hourly.getTime()));
        Reunion reunion = new Reunion(subject, hourly, room, emails);
        Log.d("TAG", reunion.toString());
        mReunionApiService.createReunion(reunion);
    }

    private void timePickerSetStatus(boolean isValid) {
        assert mBinding.btnTimePicker != null;
        if (isValid)
            mBinding.btnTimePicker.setTextColor(getResources().getColor(R.color.cyan_500));
         else
            mBinding.btnTimePicker.setTextColor(getResources().getColor(R.color.red));
    }


    private boolean isTablet() {
        return getResources().getBoolean(R.bool.isTablet);
    }

    private long convertTimeToMillis(int hour, int minute) {
        return (((hour * 60L) + minute) * 60000);
    }

    private String convertMillisToStr(long millis) {
        int heure = (int) (millis / 60000) / 60;
        int minute = (int) (millis / 60000) % 60;
        StringBuilder horaire = new StringBuilder()
                .append(heure)
                .append(":")
                .append(minute);
        if (minute == 0)
            horaire.append("0");
        return String.valueOf(horaire);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}

