package com.openclassrooms.realestatemanager;

import static com.openclassrooms.realestatemanager.Utils.ERROR_GET_BUNDLE;
import static com.openclassrooms.realestatemanager.Utils.SIGN_CHOICE;
import static com.openclassrooms.realestatemanager.Utils.SIGN_IN;
import static com.openclassrooms.realestatemanager.Utils.SIGN_UP;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.openclassrooms.realestatemanager.databinding.ActivitySignBinding;

public class SignActivity extends AppCompatActivity {

    private ActivitySignBinding binding;
    String TAG = SignActivity.this.getClass().getSimpleName();
    private String signId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Toast.makeText(this, "onCreate", Toast.LENGTH_LONG).show();

        signId = getSignId();

        if (signId != null) {
            setupLayout();
        } else {
            Toast.makeText(this, ERROR_GET_BUNDLE, Toast.LENGTH_LONG).show();
        }

    }

    private String getSignId() {
        Bundle bundle = getIntent().getExtras();
        return bundle.getString(SIGN_CHOICE);
    }

    private void setupLayout() {
        Toast.makeText(this, "updateLayout", Toast.LENGTH_LONG).show();

        if (signId.equals(SIGN_IN)) {
            updateLayoutToSignIn();
            Toast.makeText(this, "SIGN IN", Toast.LENGTH_LONG).show();
        } else if (signId.equals(SIGN_UP)) {

            updateLayoutToSignUp();
            Toast.makeText(this, "SIGN UP", Toast.LENGTH_LONG).show();
        }
    }

    private void updateLayoutToSignUp() {
        binding.notice.setText(this.getResources().getString(R.string.instructionSignUp));
        binding.btnSign.setText(this.getResources().getString(R.string.signUp));
        binding.txtFieldPsswrdConfirm.setVisibility(View.VISIBLE);
        binding.txtFieldFirstname.setVisibility(View.VISIBLE);
        binding.txtFieldLastname.setVisibility(View.VISIBLE);
    }

    private void updateLayoutToSignIn() {
        binding.notice.setText(this.getResources().getString(R.string.instructionSignIn));
        binding.btnSign.setText(this.getResources().getString(R.string.signIn));
        binding.txtFieldPsswrdConfirm.setVisibility(View.INVISIBLE);
        binding.txtFieldFirstname.setVisibility(View.INVISIBLE);
        binding.txtFieldLastname.setVisibility(View.INVISIBLE);
    }

}