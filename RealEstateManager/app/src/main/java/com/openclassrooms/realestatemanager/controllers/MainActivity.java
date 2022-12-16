package com.openclassrooms.realestatemanager.controllers;

import static com.openclassrooms.realestatemanager.utils.Utils.EMAIL;
import static com.openclassrooms.realestatemanager.utils.Utils.FIRSTNAME;
import static com.openclassrooms.realestatemanager.utils.Utils.LASTNAME;
import static com.openclassrooms.realestatemanager.utils.Utils.PASSWORD;
import static com.openclassrooms.realestatemanager.utils.Utils.SIGN_CHOICE;
import static com.openclassrooms.realestatemanager.utils.Utils.SIGN_IN;
import static com.openclassrooms.realestatemanager.utils.Utils.USER_LOGGED_FORMAT_JSON;
import static com.openclassrooms.realestatemanager.utils.Utils.clearErrorOnField;
import static com.openclassrooms.realestatemanager.utils.Utils.concatStr;
import static com.openclassrooms.realestatemanager.utils.Utils.getTodayDate;
import static com.openclassrooms.realestatemanager.utils.Utils.setErrorOnField;
import static com.openclassrooms.realestatemanager.utils.Utils.showSnackBar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.adapters.RealEstateAdapter;
import com.openclassrooms.realestatemanager.database.enumeration.TypeRealEstate;
import com.openclassrooms.realestatemanager.database.model.RealEstate;
import com.openclassrooms.realestatemanager.database.model.User;
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;
import com.openclassrooms.realestatemanager.fragments.RealEstateDetailFragment;
import com.openclassrooms.realestatemanager.fragments.RealEstateListFragment;
import com.openclassrooms.realestatemanager.viewModel.RealEstateViewModel;
import com.openclassrooms.realestatemanager.viewModel.UserViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, RealEstateAdapter.OnRealEstateListener {

    String TAG = MainActivity.this.getClass().getSimpleName();

    private ActivityMainBinding binding;
    private UserViewModel userViewModel;
    private RealEstateViewModel realEstateViewModel;

    private boolean userViewer;

    private RealEstateListFragment realEstateListFragment;
    private RealEstateDetailFragment realEstateDetailFragment;

    private final Map<String, String> maskFieldsSettings = new HashMap<>();

    private Dialog customDialogSettings, customDialogHomeLoan;

    private ImageView picture;
    private TextInputLayout editTxtFirstname;
    private TextInputLayout editTxtLastname;
    private TextInputLayout editTxtEmail;
    private TextInputLayout editTxtPassword;

    private Uri selectedImageUri;
    private long id = 0L;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        this.messageLogin();
        this.initViewModel();
        this.showFragmentsFirstTime();
        this.configureMenu();
    }

    private void messageLogin() {
        String msg = getIntent().getExtras().getString(SIGN_CHOICE).equals(SIGN_IN) ? getResources().getString(R.string.sign_in_successfull) : getResources().getString(R.string.sign_up_successfull);
        showSnackBar(binding.mainLayout, msg);
    }

    private void initViewModel() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        realEstateViewModel = new ViewModelProvider(this).get(RealEstateViewModel.class);
    }

    private void showFragmentsFirstTime() {
        getAllRealEstates();
        binding.activityMainNavView.getMenu().getItem(0).setChecked(true);
        setupRealEstateDetailFragmentAndShow();
    }

    private void setupRealEstateDetailFragmentAndShow() {

        realEstateDetailFragment = (RealEstateDetailFragment) getSupportFragmentManager().findFragmentById(binding.realEstatesDetailFrameLayout.getId());

        if (realEstateListFragment == null || id > 0) {
            realEstateDetailFragment = RealEstateDetailFragment.newInstance();

            realEstateViewModel.getRealEstateById(id).observe(this, realEstate -> {

                realEstateDetailFragment.setRealEstate(realEstate);
                getSupportFragmentManager().beginTransaction().replace(R.id.real_estates_detail_frame_layout, realEstateDetailFragment).commit();
            });

        }
    }

    private void getAllRealEstates() {

        userViewer = false;
        realEstateViewModel.getAllRealEstates().observe(this, realEstates -> {

            if (!userViewer) {
                setupRealEstateListFragmentAndShow(realEstates);
            }
        });
    }

    private void setupRealEstateListFragmentAndShow(List<RealEstate> realEstates) {

        if (realEstateListFragment == null) {

            realEstateListFragment = (RealEstateListFragment) getSupportFragmentManager().findFragmentById(binding.realEstatesListFrameLayout.getId());

            realEstateListFragment = RealEstateListFragment.newInstance(realEstates, this);
            getSupportFragmentManager().beginTransaction().replace(R.id.real_estates_list_frame_layout, realEstateListFragment).commit();
        } else {
            realEstateListFragment.updateList(realEstates);
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.add_realestate) {
            Toast.makeText(this, "Click on Add", Toast.LENGTH_SHORT).show();

            RealEstate newRealEstate = new RealEstate(null, "test", 150, TypeRealEstate.House, 50, 2, 4, "description", "address", false, getTodayDate());
            realEstateViewModel.insert(newRealEstate);

        } else if (item.getItemId() == R.id.edit_realestate) {
            Toast.makeText(this, "Click on Edit", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Click on Search", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
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

        TextView userName = accessMenuHeaderInfo().findViewById(R.id.user_Name);
        TextView userEmail = accessMenuHeaderInfo().findViewById(R.id.user_Email);

        long id = getIdUserLogged();
        userViewModel.getUserById(id).observe(this, user -> {

            userName.setText(concatStr(user.getFirstname(), user.getLastname()));
            userEmail.setText(user.getEmail());
            if (doesUserHaveAPicture(user)) {
                ImageView picture = accessMenuHeaderInfo().findViewById(R.id.user_Picture);
                setProfilePicture(user.getPicture(), picture);
            }
        });
    }

    private View accessMenuHeaderInfo() {
        return binding.activityMainNavView.getHeaderView(0);
    }

    private long getIdUserLogged() {
        return getUserJsonInUserObject().getId();
    }

    private User getUserJsonInUserObject() {

        Gson gson = new Gson();
        return gson.fromJson(getIntent().getStringExtra(USER_LOGGED_FORMAT_JSON), User.class);
    }

    private boolean doesUserHaveAPicture(User user) {
        return user.getPicture() != null;
    }

    private void setProfilePicture(String url, ImageView picture) {
        Glide.with(this)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .apply(RequestOptions.circleCropTransform())
                .into(picture);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_Item_0) {
            getAllRealEstates();

        } else if (id == R.id.menu_Item_1) {
            getAllRealEstatesAssociatedWithAUser();

        } else if (id == R.id.menu_Item_2) {

            customDialogSettings = getDialogSetting(R.layout.settings_layout);
            initViewDialogSetting();
            setupListenerDialogSettings();
            customDialogSettings.show();

        } else if (id == R.id.menu_Item_3) {

            customDialogHomeLoan = getDialogSetting(R.layout.home_loan_layout);
            setupDialogHomeLoan();
            customDialogHomeLoan.show();

        } else {
            setupListenerDialogLogout();
        }

        binding.mainLayout.closeDrawer(GravityCompat.START);

        return true;
    }


    private void getAllRealEstatesAssociatedWithAUser() {

        long id = getIdUserLogged();
        realEstateViewModel.getRealEstateByUserId(id).observe(this, this::setupRealEstateListFragmentAndShow);
    }

    private Dialog getDialogSetting(int layoutId) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layoutId);
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return dialog;
    }


    private void initViewDialogSetting() {
        picture = customDialogSettings.findViewById(R.id.user_Picture);
        editTxtFirstname = customDialogSettings.findViewById(R.id.txtFieldFirstname);
        editTxtLastname = customDialogSettings.findViewById(R.id.txtFieldLastname);
        editTxtEmail = customDialogSettings.findViewById(R.id.txtFieldEmail);
        editTxtPassword = customDialogSettings.findViewById(R.id.txtFieldPsswrd);
    }

    private void setupListenerDialogSettings() {

        ImageButton close = customDialogSettings.findViewById(R.id.close_Settings);
        close.setOnClickListener(v -> customDialogSettings.dismiss());


        picture.setOnClickListener(v -> imageChooser());

        long id = getIdUserLogged();
        userViewModel.getUserById(id).observe(this, user -> {

            updateCustomDialogSettings(user);
            Button btnSave = customDialogSettings.findViewById(R.id.btnSave);
            btnSave.setOnClickListener(v -> {

                setMaskFieldsSettings();

                if (!areTheFieldsEmpty() && !isUserEmailExistAlready(user) && emailValid()) {
                    updateUserAccount(user);
                    customDialogSettings.dismiss();
                }
            });
        });
    }

    private void setupDialogHomeLoan() {

        ImageButton close = customDialogHomeLoan.findViewById(R.id.close_Settings);
        close.setOnClickListener(v -> customDialogHomeLoan.dismiss());

        TextInputLayout txtInputCredit = customDialogHomeLoan.findViewById(R.id.credit);
        Slider slideInputYear = customDialogHomeLoan.findViewById(R.id.slider_years);
        Slider slideInterestRate = customDialogHomeLoan.findViewById(R.id.slider_txtInterestRate);

        Button btnCalculHomeLoan = customDialogHomeLoan.findViewById(R.id.btnCalcul);
        TextView resultMonthly = customDialogHomeLoan.findViewById(R.id.monthly_refund);

        btnCalculHomeLoan.setOnClickListener(v -> {

            boolean allFieldsFill = !Objects.requireNonNull(txtInputCredit.getEditText()).getText().toString().isEmpty() && slideInputYear.getValue() > 0 && slideInterestRate.getValue() > 0;

            if (allFieldsFill) {

                double creditAmount = Float.parseFloat(Objects.requireNonNull(txtInputCredit.getEditText()).getText().toString());
                double interestRate = Float.parseFloat(String.valueOf(slideInterestRate.getValue())) / 100;
                double nbYearsOfRefundInMonth = (slideInputYear.getValue()) * 12;
                int monthlyRefund = (int) Math.round(((creditAmount * interestRate) / 12.0) / (1 - (Math.pow((1 + (interestRate / 12.0)), (-1 * nbYearsOfRefundInMonth)))));

                resultMonthly.setVisibility(View.VISIBLE);
                resultMonthly.setText(getResources().getString(R.string.by_month, monthlyRefund));
            } else {
                Toast.makeText(this, getResources().getString(R.string.error_fill_all_field), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void imageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        launchSomeActivity.launch(i);
    }

    ActivityResultLauncher<Intent> launchSomeActivity
            = registerForActivityResult(
            new ActivityResultContracts
                    .StartActivityForResult(),
            result -> {
                if (result.getResultCode()
                        == Activity.RESULT_OK) {
                    Intent data = result.getData();

                    if (data != null
                            && data.getData() != null) {
                        long id = getIdUserLogged();
                        userViewModel.getUserById(id).observe(this, user -> {
                            selectedImageUri = data.getData();
                            user.setPicture(selectedImageUri.toString());
                            setProfilePicture(user.getPicture(), picture);
                        });
                    }
                }
            });

    private void updateCustomDialogSettings(User user) {

        if (doesUserHaveAPicture(user)) {
            setProfilePicture(user.getPicture(), picture);
        }
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
                setErrorOnField(customDialogSettings, key, getResources().getString(R.string.field_is_requiered));
                isEmpty = true;
            } else {
                clearErrorOnField(customDialogSettings, key);
            }
        }
        return isEmpty;
    }

    private boolean isUserEmailExistAlready(User user) {
        boolean isUserEmailExistYet = userViewModel.isUserEmailExistAlready(user);
        if (isUserEmailExistYet) {
            setErrorOnField(customDialogSettings, EMAIL, getResources().getString(R.string.email_exists_already));
        }
        return isUserEmailExistYet;
    }

    private boolean emailValid() {

        boolean emailFormatValid = Patterns.EMAIL_ADDRESS.matcher(Objects.requireNonNull(maskFieldsSettings.get(EMAIL))).matches();
        if (!emailFormatValid) {
            setErrorOnField(customDialogSettings, EMAIL, getResources().getString(R.string.email_invalid));
        }
        return emailFormatValid;
    }


    private void updateUserAccount(User user) {

        if (selectedImageUri != null) {
            user.setPicture(selectedImageUri.toString());
        }
        user.setFirstname(Objects.requireNonNull(maskFieldsSettings.get(FIRSTNAME)));
        user.setLastname(Objects.requireNonNull(maskFieldsSettings.get(LASTNAME)));
        user.setEmail(Objects.requireNonNull(maskFieldsSettings.get(EMAIL)));
        user.setPassword(Objects.requireNonNull(maskFieldsSettings.get(PASSWORD)));
        int nbUpdate = userViewModel.update(user);
        if (nbUpdate > 0) {
            showSnackBar(binding.mainLayout, getResources().getString(R.string.account_updated));

        }
    }

    private void setupListenerDialogLogout() {

        new AlertDialog.Builder(this)
                .setMessage(getResources().getString(R.string.do_you_want_disconnected))
                .setPositiveButton(getResources().getString(R.string.yes), (dialogInterface, i) -> finish()
                )
                .setNegativeButton(getResources().getString(R.string.no), null)
                .show();
    }

    @Override
    public void onBackPressed() {
        // 5 - Handle back click to close menu
        if (binding.mainLayout.isDrawerOpen(GravityCompat.START)) {
            binding.mainLayout.closeDrawer(GravityCompat.START);
        } else {
            showSnackBar(binding.mainLayout, getResources().getString(R.string.please_disconnected_you));
        }
    }

    @Override
    public void onRealEstateClick(long id) {
        this.id = id;
        setupRealEstateDetailFragmentAndShow();
    }
}
