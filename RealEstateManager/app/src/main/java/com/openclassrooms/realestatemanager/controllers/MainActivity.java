package com.openclassrooms.realestatemanager.controllers;

import static com.openclassrooms.realestatemanager.utils.Utils.EMAIL;
import static com.openclassrooms.realestatemanager.utils.Utils.FIRSTNAME;
import static com.openclassrooms.realestatemanager.utils.Utils.LASTNAME;
import static com.openclassrooms.realestatemanager.utils.Utils.PASSWORD;
import static com.openclassrooms.realestatemanager.utils.Utils.USER_LOGGED_FORMAT_JSON;
import static com.openclassrooms.realestatemanager.utils.Utils.clearErrorOnField;
import static com.openclassrooms.realestatemanager.utils.Utils.concatStr;
import static com.openclassrooms.realestatemanager.utils.Utils.getDialogSetting;
import static com.openclassrooms.realestatemanager.utils.Utils.setErrorOnField;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.database.model.User;
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.viewModel.UserViewModel;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    String TAG = MainActivity.this.getClass().getSimpleName();

    private ActivityMainBinding binding;
    private UserViewModel userViewModel;

    private final Map<String, String> maskFieldsSettings = new HashMap<>();

    private Dialog customDialogSettings;
    private TextInputLayout editTxtFirstname;
    private TextInputLayout editTxtLastname;
    private TextInputLayout editTxtEmail;
    private TextInputLayout editTxtPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        this.initViewModel();
        this.configureTextViewMain();
        this.configureTextViewQuantity();
        this.configureMenu();
    }

    private void initViewModel() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    private void configureTextViewMain() {
//        binding.activityMainActivityTextViewMain.setTextSize(Float.parseFloat("15"));
//        binding.activityMainActivityTextViewMain.setText("Le premier bien immobilier enregistré vaut ");
    }

    private void configureTextViewQuantity() {
        int quantity = Utils.convertDollarToEuro(100);
//        binding.activityMainActivityTextViewQuantity.setTextSize(Float.parseFloat("20"));
//        binding.activityMainActivityTextViewQuantity.setText(String.valueOf(quantity));
    }

    private void configureMenu() {
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();
        this.updateMenuWithUserData();
    }

    private void configureToolBar() {
        binding.activityMainToolbar.setTitle(R.string.app_name);
        setSupportActionBar(binding.activityMainToolbar);
    }

    private void configureDrawerLayout() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.mainLayout, binding.activityMainToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.mainLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void configureNavigationView() {
        binding.activityMainNavView.setNavigationItemSelectedListener(this);
    }

    private void updateMenuWithUserData() {

        long id = getUserJsonInUserObject().getId();
        TextView userName = accessMenuHeaderInfo().findViewById(R.id.user_Name);
        TextView userEmail = accessMenuHeaderInfo().findViewById(R.id.user_Email);

        userViewModel.getUserById(id).observe(this, user -> {
            userName.setText(concatStr(user.getFirstname(), user.getLastname()));
            userEmail.setText(user.getEmail());
            if (user.getPicture() != null) {
                setProfilePicture(user);
            }
        });
    }

    private User getUserJsonInUserObject() {

        Gson gson = new Gson();
        return gson.fromJson(getIntent().getStringExtra(USER_LOGGED_FORMAT_JSON), User.class);
    }

    private View accessMenuHeaderInfo() {
        return binding.activityMainNavView.getHeaderView(0);
    }

    private void setProfilePicture(User user) {
        Glide.with(this)
                .load(user.getPicture())
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .apply(RequestOptions.circleCropTransform())
                .into((ImageView) accessMenuHeaderInfo().findViewById(R.id.user_Picture));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_Item_1) {
            Toast.makeText(this, "Click On Item 1", Toast.LENGTH_SHORT).show();


        } else if (id == R.id.menu_Item_2) {
            customDialogSettings = getDialogSetting(MainActivity.this);
            initViewDialogSetting();
            setupListenerDialogSettings();
            customDialogSettings.show();


        } else if (id == R.id.menu_Item_3) {
            Toast.makeText(this, "Click On Item 3", Toast.LENGTH_SHORT).show();

        }

        binding.mainLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    private void initViewDialogSetting() {
        editTxtFirstname = customDialogSettings.findViewById(R.id.txtFieldFirstname);
        editTxtLastname = customDialogSettings.findViewById(R.id.txtFieldLastname);
        editTxtEmail = customDialogSettings.findViewById(R.id.txtFieldEmail);
        editTxtPassword = customDialogSettings.findViewById(R.id.txtFieldPsswrd);
    }

    private void setupListenerDialogSettings() {

        ImageButton close = customDialogSettings.findViewById(R.id.close_Settings);
        close.setOnClickListener(v -> customDialogSettings.dismiss());


        long id = getUserJsonInUserObject().getId();
        userViewModel.getUserById(id).observe(this, user -> {

            fillFieldsCustomDialogSettings(user);

            Button btnSave = customDialogSettings.findViewById(R.id.btnSave);
            btnSave.setOnClickListener(v -> {

                setMaskFieldsSettings();

                if (!areTheFieldsEmpty() && !isUserEmailExistAlready() && emailValid()) {
                    updateUserAccount(user);
                    customDialogSettings.dismiss();

                }
            });
        });
    }

    private void fillFieldsCustomDialogSettings(User user) {

        Objects.requireNonNull(editTxtFirstname.getEditText()).setText(user.getFirstname());
        Objects.requireNonNull(editTxtLastname.getEditText()).setText(user.getLastname());
        Objects.requireNonNull(editTxtEmail.getEditText()).setText(user.getEmail());
        Objects.requireNonNull(editTxtPassword.getEditText()).setText(user.getPassword());
    }

    private void setMaskFieldsSettings() {
        maskFieldsSettings.put(FIRSTNAME, Objects.requireNonNull(editTxtFirstname.getEditText()).getText().toString().trim());
        maskFieldsSettings.put(LASTNAME, Objects.requireNonNull(editTxtLastname.getEditText()).getText().toString().trim());
        maskFieldsSettings.put(EMAIL, Objects.requireNonNull(editTxtEmail.getEditText()).getText().toString().trim());
        maskFieldsSettings.put(PASSWORD, Objects.requireNonNull(editTxtPassword.getEditText()).getText().toString().trim());
    }


    private boolean areTheFieldsEmpty() {

        boolean isEmpty = false;
        for (String key : maskFieldsSettings.keySet()) {

            if (Objects.requireNonNull(maskFieldsSettings.get(key)).isEmpty()) {
                setErrorOnField(customDialogSettings, key, "* This field is requiered");
                isEmpty = true;
            } else {
                clearErrorOnField(customDialogSettings, key);
            }
        }
        return isEmpty;
    }

    private boolean isUserEmailExistAlready() {
        boolean isUserEmailExistYet = userViewModel.checkIfEmailExistYet(maskFieldsSettings.get(EMAIL));
        if (isUserEmailExistYet) {
            setErrorOnField(customDialogSettings,EMAIL, "A user with that e-mail already exists");
        }
        return isUserEmailExistYet;
    }

    private boolean emailValid() {

        boolean emailFormatValid = Patterns.EMAIL_ADDRESS.matcher(Objects.requireNonNull(maskFieldsSettings.get(EMAIL))).matches();
        if (!emailFormatValid) {
            setErrorOnField(customDialogSettings,EMAIL, "Email invalid");
        }
        return emailFormatValid;
    }


    private void updateUserAccount(User user) {
        //TODO When user change email check if email exist yet if setError Also Update Account
        //TODO Change image when user click on img rounded, user.setPicture("url")

        user.setFirstname(Objects.requireNonNull(maskFieldsSettings.get(FIRSTNAME)));
        user.setLastname(Objects.requireNonNull(maskFieldsSettings.get(LASTNAME)));
        user.setEmail(Objects.requireNonNull(maskFieldsSettings.get(EMAIL)));
        user.setPassword(Objects.requireNonNull(maskFieldsSettings.get(PASSWORD)));
        userViewModel.update(user);
        Toast.makeText(this, "Account updated", Toast.LENGTH_SHORT).show();
    }
}
