package com.openclassrooms.realestatemanager.controllers;

import static com.openclassrooms.realestatemanager.utils.Utils.EMAIL;
import static com.openclassrooms.realestatemanager.utils.Utils.FIRSTNAME;
import static com.openclassrooms.realestatemanager.utils.Utils.LASTNAME;
import static com.openclassrooms.realestatemanager.utils.Utils.PASSWORD;
import static com.openclassrooms.realestatemanager.utils.Utils.PICTURE_GALLERY;
import static com.openclassrooms.realestatemanager.utils.Utils.PICTURE_USER;
import static com.openclassrooms.realestatemanager.utils.Utils.SIGN_CHOICE;
import static com.openclassrooms.realestatemanager.utils.Utils.SIGN_IN;
import static com.openclassrooms.realestatemanager.utils.Utils.USER_LOGGED_FORMAT_JSON;
import static com.openclassrooms.realestatemanager.utils.Utils.atLeastOneFieldToUpdateIsEmpty;
import static com.openclassrooms.realestatemanager.utils.Utils.castDoubleToInt;
import static com.openclassrooms.realestatemanager.utils.Utils.clearErrorOnField;
import static com.openclassrooms.realestatemanager.utils.Utils.concatStr;
import static com.openclassrooms.realestatemanager.utils.Utils.convertCurrency;
import static com.openclassrooms.realestatemanager.utils.Utils.convertEuroToDollar;
import static com.openclassrooms.realestatemanager.utils.Utils.convertToString;
import static com.openclassrooms.realestatemanager.utils.Utils.getDialog;
import static com.openclassrooms.realestatemanager.utils.Utils.getTodayDate;
import static com.openclassrooms.realestatemanager.utils.Utils.setErrorOnField;
import static com.openclassrooms.realestatemanager.utils.Utils.setProfilePicture;
import static com.openclassrooms.realestatemanager.utils.Utils.setRealEstatePicture;
import static com.openclassrooms.realestatemanager.utils.Utils.setupListenerCloseBtn;
import static com.openclassrooms.realestatemanager.utils.Utils.showSnackBar;
import static com.openclassrooms.realestatemanager.utils.Utils.showToast;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.adapters.RealEstateAdapter;
import com.openclassrooms.realestatemanager.database.enumeration.Currency;
import com.openclassrooms.realestatemanager.database.enumeration.TypeRealEstate;
import com.openclassrooms.realestatemanager.database.model.Image;
import com.openclassrooms.realestatemanager.database.model.RealEstate;
import com.openclassrooms.realestatemanager.database.model.User;
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;
import com.openclassrooms.realestatemanager.fragments.RealEstateDetailFragment;
import com.openclassrooms.realestatemanager.fragments.RealEstateListFragment;
import com.openclassrooms.realestatemanager.viewModel.RealEstateViewModel;
import com.openclassrooms.realestatemanager.viewModel.UserViewModel;

import java.util.ArrayList;
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

    private Dialog customDialog;

    private ImageView picture;
    private TextInputLayout editTxtFirstname;
    private TextInputLayout editTxtLastname;
    private TextInputLayout editTxtEmail;
    private TextInputLayout editTxtPassword;

    private Uri selectedImageUri;
    private long id = 1L;


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


        if (realEstateDetailFragment == null) {
            realEstateDetailFragment = RealEstateDetailFragment.newInstance();

            getSupportFragmentManager().beginTransaction().replace(R.id.real_estates_detail_frame_layout, realEstateDetailFragment).commit();
        }
        if (id > 0) {
            realEstateViewModel.getRealEstateById(id).observe(this, realEstate -> {

                realEstateViewModel.getRealEstateImages(realEstate).observe(this, images -> {


                    realEstateDetailFragment.setRealEstate(realEstate, images);
                });
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

            customDialog = getDialog(MainActivity.this, R.layout.edit_real_estate_layout);
            setupDialogAddRealEstate();
            customDialog.show();

        } else if (item.getItemId() == R.id.edit_realestate) {
            customDialog = getDialog(MainActivity.this, R.layout.edit_real_estate_layout);
            setupDialogUpdateRealEstate();
            customDialog.show();

        } else {
            Toast.makeText(this, "Click on Search", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDialogAddRealEstate() {

        setupListenerCloseBtn(customDialog);
        this.changeUpdateLayoutToAddLayout();

        Spinner spinner = customDialog.findViewById(R.id.typeRealEstate);
        spinner.setVisibility(View.VISIBLE);
        // Spinner Drop down elements
        List<String> types = new ArrayList<>();
        types.add(TypeRealEstate.House.toString());
        types.add(TypeRealEstate.Loft.toString());
        types.add(TypeRealEstate.Manoir.toString());
        types.add(TypeRealEstate.Penthouse.toString());
        types.add(TypeRealEstate.Duplex.toString());
        types.add(TypeRealEstate.Flat.toString());


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        TextInputLayout price = customDialog.findViewById(R.id.txtFieldPrice);
        TextInputLayout description = customDialog.findViewById(R.id.txtFieldDescription);
        TextInputLayout address = customDialog.findViewById(R.id.txtFieldLocation);
        TextInputLayout surface = customDialog.findViewById(R.id.txtFieldSurface);
        TextInputLayout rooms = customDialog.findViewById(R.id.txtFieldRooms);
        TextInputLayout bathrooms = customDialog.findViewById(R.id.txtFieldBathrooms);
        TextInputLayout bedrooms = customDialog.findViewById(R.id.txtFieldBedrooms);
        TextInputLayout interestPoints = customDialog.findViewById(R.id.txtFieldInterestPoint);

        TextInputLayout[] fields = {price, description, address, surface, rooms, bathrooms, bedrooms, interestPoints};
        customDialog.findViewById(R.id.update_btn).setOnClickListener(v -> {
            String msgFieldsRequired = getResources().getString(R.string.field_is_requiered);

            if (!atLeastOneFieldToUpdateIsEmpty(customDialog, fields, msgFieldsRequired)) {
                long agentId = getIdUserLogged();
                double priceValue = Double.parseDouble(Objects.requireNonNull(price.getEditText()).getText().toString());
                int surfaceValue = Integer.parseInt(Objects.requireNonNull(surface.getEditText()).getText().toString());
                int roomsValue = Integer.parseInt(Objects.requireNonNull(rooms.getEditText()).getText().toString());
                int bedroomsValue = Integer.parseInt(Objects.requireNonNull(bedrooms.getEditText()).getText().toString());
                int bathroomsValue = Integer.parseInt(Objects.requireNonNull(bathrooms.getEditText()).getText().toString());
                String descriptionValue = Objects.requireNonNull(description.getEditText()).getText().toString();
                String addressValue = Objects.requireNonNull(address.getEditText()).getText().toString();
                String interestPointValue = Objects.requireNonNull(interestPoints.getEditText()).getText().toString();

                RealEstate newRealEstate = new RealEstate(agentId, "Name", priceValue, spinner.getSelectedItem().toString(), surfaceValue, roomsValue, bedroomsValue, bathroomsValue, descriptionValue, addressValue, false, getTodayDate(), interestPointValue);

                this.addRealEstate(newRealEstate);
            }
        });
    }

    private void changeUpdateLayoutToAddLayout() {
        TextView title = customDialog.findViewById(R.id.edition);
        Button btn = customDialog.findViewById(R.id.update_btn);

        customDialog.findViewById(R.id.switch_sold).setVisibility(View.GONE);
        customDialog.findViewById(R.id.txtFieldDate).setVisibility(View.GONE);

        title.setText(getResources().getString(R.string.add_real_estate));
        btn.setText(getResources().getString(R.string.add_btn));
    }

    private void setupDialogUpdateRealEstate() {

        String fieldRequired = getResources().getString(R.string.field_is_requiered);

        realEstateViewModel.getRealEstateById(id).observe(this, realEstate -> {
            setImagesRealEstate(realEstate);

            SwitchCompat soldSwitch = customDialog.findViewById(R.id.switch_sold);
            soldSwitch.setChecked(realEstate.getSold());

            TextInputLayout price = customDialog.findViewById(R.id.txtFieldPrice);
            TextInputLayout description = customDialog.findViewById(R.id.txtFieldDescription);
            TextInputLayout date = customDialog.findViewById(R.id.txtFieldDate);
            TextInputLayout address = customDialog.findViewById(R.id.txtFieldLocation);
            TextInputLayout surface = customDialog.findViewById(R.id.txtFieldSurface);
            TextInputLayout rooms = customDialog.findViewById(R.id.txtFieldRooms);
            TextInputLayout bathrooms = customDialog.findViewById(R.id.txtFieldBathrooms);
            TextInputLayout bedrooms = customDialog.findViewById(R.id.txtFieldBedrooms);
            TextInputLayout interestPoints = customDialog.findViewById(R.id.txtFieldInterestPoint);


            setValueForTextInputLayout(price, realEstate);
            setValueForTextInputLayout(description, realEstate);
            setValueForTextInputLayout(date, realEstate);
            setValueForTextInputLayout(address, realEstate);
            setValueForTextInputLayout(surface, realEstate);
            setValueForTextInputLayout(rooms, realEstate);
            setValueForTextInputLayout(bathrooms, realEstate);
            setValueForTextInputLayout(bedrooms, realEstate);
            setValueForTextInputLayout(interestPoints, realEstate);

            customDialog.findViewById(R.id.update_btn).setOnClickListener(v -> {


                int numberOfImage = realEstateViewModel.getNumberOfImages(this.id);
                if (numberOfImage > 0) {
                    TextInputLayout[] fieldsInputLayout = {price, description, date, address, surface, rooms, bathrooms, bedrooms, interestPoints};

                    boolean isInputHasZeroAsValue = DoesOneInputHasValueToZero(customDialog, fieldsInputLayout);
                    if (!isInputHasZeroAsValue) {

                        if (!atLeastOneFieldToUpdateIsEmpty(customDialog, fieldsInputLayout, fieldRequired)) {

                            double priceChanged = Double.parseDouble(Objects.requireNonNull(price.getEditText()).getText().toString());
                            int surfaceChanged = Integer.parseInt(Objects.requireNonNull(surface.getEditText()).getText().toString());
                            int roomsChanged = Integer.parseInt(Objects.requireNonNull(rooms.getEditText()).getText().toString());
                            int bedroomsChanged = Integer.parseInt(Objects.requireNonNull(bedrooms.getEditText()).getText().toString());
                            int bathroomsChanged = Integer.parseInt(Objects.requireNonNull(bathrooms.getEditText()).getText().toString());
                            String descriptionChanged = Objects.requireNonNull(description.getEditText()).getText().toString();
                            String dateChanged = Objects.requireNonNull(date.getEditText()).getText().toString();
                            String addressChanged = Objects.requireNonNull(address.getEditText()).getText().toString();
                            String interestPointChanged = Objects.requireNonNull(interestPoints.getEditText()).getText().toString();

                            RealEstate realEstateToUpdate = new RealEstate(realEstate.getAgentId(), realEstate.getName(), priceChanged, realEstate.getTypeRealEstate(), surfaceChanged, roomsChanged, bedroomsChanged, bathroomsChanged, descriptionChanged, addressChanged, soldSwitch.isChecked(), dateChanged, interestPointChanged);

                            if (soldSwitch.isChecked()) {
                                realEstateToUpdate.setDateOfSell(getTodayDate());
                            }
                            realEstateToUpdate.setId(realEstate.getId());

                            this.updateRealEstate(realEstateToUpdate);

                        }
                    }
                } else {
                    showToast(this, getResources().getString(R.string.keep_at_least_one_image));
                }
            });
        });
    }

    private boolean DoesOneInputHasValueToZero(Dialog dialog, TextInputLayout[] fields) {
        for (TextInputLayout field : fields) {
            EditText editText = field.getEditText();
            if (Objects.requireNonNull(editText).getInputType() == InputType.TYPE_CLASS_NUMBER) {
                if (editText.getText().toString().equals("0")) {
                    setErrorOnField(dialog, field.getId(), getResources().getString(R.string.zero_value));
                    return true;
                }else{
                    clearErrorOnField(dialog, field.getId());

                }
            }
        }
        return false;
    }

    private void setImagesRealEstate(RealEstate realEstate) {

        LayoutInflater mInflater = LayoutInflater.from(this);
        LinearLayout gallery = customDialog.findViewById(R.id.id_gallery);
        realEstateViewModel.getRealEstateImages(realEstate).observe(this, images -> {
            gallery.removeAllViews();

            setImageViewAddPicture(mInflater, gallery);

            for (Image image : images) {

                View view = mInflater.inflate(R.layout.gallery_item, gallery, false);

                ImageView img = view.findViewById(R.id.id_gallery_item_image);
                ImageButton btnClose = view.findViewById(R.id.btn_close_thumb);

                setRealEstatePicture(this, image.getUrl(), img);
                btnClose.setVisibility(View.VISIBLE);
                gallery.addView(view);

                img.setOnClickListener(v -> zoomInImage(image));

                btnClose.setOnClickListener(v -> removeImage(view, gallery, img, image));
            }
        });
    }

    private void setImageViewAddPicture(LayoutInflater inflater, LinearLayout gallery) {

        View view = inflater.inflate(R.layout.gallery_item, gallery, false);
        ImageView imgAdd = view.findViewById(R.id.id_gallery_item_image);
        String url = "https://static.thenounproject.com/png/3322766-200.png";
        setRealEstatePicture(this, url, imgAdd);
        gallery.addView(view);
        imgAdd.setOnClickListener(v -> imageChooser(PICTURE_GALLERY));
    }

    private void zoomInImage(Image image) {
        customDialog = getDialog(this, R.layout.image_zoomed_layout);
        ImageView imageZoomed = customDialog.findViewById(R.id.image_zoomed);
        setRealEstatePicture(this, image.getUrl(), imageZoomed);
        customDialog.show();
    }

    private void removeImage(View view, LinearLayout gallery, ImageView imgView, Image image) {
        gallery.removeView(view);
        view.setVisibility(View.GONE);
        imgView.setVisibility(View.GONE);
        this.deleteImage(image);
    }

    private void deleteImage(Image image) {
        int id = realEstateViewModel.deleteRealEstateImages(image);
        if (id > 0) {
            showToast(this, getResources().getString(R.string.image_deleted));
        }

    }


    private void setValueForTextInputLayout(TextInputLayout input, RealEstate realEstate) {

        int id = input.getId();

        if (id == R.id.txtFieldPrice) {
            Objects.requireNonNull(input.getEditText()).setText(convertToString(castDoubleToInt(realEstate.getPrice())));

        } else if (id == R.id.txtFieldDescription) {
            Objects.requireNonNull(input.getEditText()).setText(realEstate.getDescription());

        } else if (id == R.id.txtFieldDate) {
            Objects.requireNonNull(input.getEditText()).setText(realEstate.getDateOfEntry());

        } else if (id == R.id.txtFieldLocation) {
            Objects.requireNonNull(input.getEditText()).setText(realEstate.getAddress());

        } else if (id == R.id.txtFieldSurface) {
            Objects.requireNonNull(input.getEditText()).setText(convertToString(realEstate.getSurface()));

        } else if (id == R.id.txtFieldRooms) {
            Objects.requireNonNull(input.getEditText()).setText(convertToString(realEstate.getNbRoom()));

        } else if (id == R.id.txtFieldBathrooms) {
            Objects.requireNonNull(input.getEditText()).setText(convertToString(realEstate.getNbBathRoom()));

        } else if (id == R.id.txtFieldBedrooms) {
            Objects.requireNonNull(input.getEditText()).setText(convertToString(realEstate.getNbBedRoom()));
        } else {
            Objects.requireNonNull(input.getEditText()).setText(realEstate.getInterestPoint());

        }
    }

    private void updateRealEstate(@NonNull RealEstate realEstate) {
        int updated = realEstateViewModel.update(realEstate);
        customDialog.dismiss();
        showToast(this, getResources().getString(R.string.real_estate_updated));
    }

    private void addRealEstate(@NonNull RealEstate realEstate) {
        long id = realEstateViewModel.insert(realEstate);
        customDialog.dismiss();
        if (id == -1) {
            showToast(this, getResources().getString(R.string.real_estate_not_added));
        } else {
            showToast(this, getResources().getString(R.string.real_estate_added));
        }
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
                setProfilePicture(this, user.getPicture(), picture);
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


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_Item_0) {
            getAllRealEstates();

        } else if (id == R.id.menu_Item_1) {
            getAllRealEstatesAssociatedWithAUser();

        } else if (id == R.id.menu_Item_2) {

            customDialog = getDialog(MainActivity.this, R.layout.settings_layout);
            initViewDialogSetting();
            setupListenerDialogSettings();
            customDialog.show();

        } else if (id == R.id.menu_Item_3) {

            customDialog = getDialog(MainActivity.this, R.layout.home_loan_layout);
            setupDialogHomeLoan();
            customDialog.show();

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


    private void initViewDialogSetting() {
        picture = customDialog.findViewById(R.id.user_Picture);
        editTxtFirstname = customDialog.findViewById(R.id.txtFieldFirstname);
        editTxtLastname = customDialog.findViewById(R.id.txtFieldLastname);
        editTxtEmail = customDialog.findViewById(R.id.txtFieldEmail);
        editTxtPassword = customDialog.findViewById(R.id.txtFieldPsswrd);
    }

    private void setupListenerDialogSettings() {

        setupListenerCloseBtn(customDialog);

        picture.setOnClickListener(v -> imageChooser(PICTURE_USER));

        long id = getIdUserLogged();
        userViewModel.getUserById(id).observe(this, user -> {

            updateCustomDialogSettings(user);
            Button btnSave = customDialog.findViewById(R.id.btnSave);
            btnSave.setOnClickListener(v -> {

                setMaskFieldsSettings();

                if (!areTheFieldsEmpty() && !isUserEmailExistAlready(user) && emailValid()) {
                    updateUserAccount(user);
                    customDialog.dismiss();
                }
            });
        });
    }

    private void setupDialogHomeLoan() {

        setupListenerCloseBtn(customDialog);

        SwitchCompat switchCurrency = customDialog.findViewById(R.id.switchCurrency);
        TextInputLayout txtInputCredit = customDialog.findViewById(R.id.credit);
        Slider slideInputYear = customDialog.findViewById(R.id.slider_years);
        Slider slideInterestRate = customDialog.findViewById(R.id.slider_txtInterestRate);

        Button btnCalculateHomeLoan = customDialog.findViewById(R.id.btnCalcul);
        TextView resultMonthly = customDialog.findViewById(R.id.monthly_refund);

        switchCurrency.setOnClickListener(v -> {

            if (switchCurrency.isChecked()) {
                switchCurrency.setText(getResources().getString(R.string.euros));
                txtInputCredit.setHint(getResources().getString(R.string.amount_of_the_loan_euros));

                String inputValue = Objects.requireNonNull(txtInputCredit.getEditText()).getText().toString();
                String dollars = convertCurrency(inputValue, Currency.dollar);
                txtInputCredit.getEditText().setText(dollars);

            } else {
                switchCurrency.setText(getResources().getString(R.string.dollars));
                txtInputCredit.setHint(getResources().getString(R.string.amount_of_the_loan_dollars));
                String inputValue = Objects.requireNonNull(txtInputCredit.getEditText()).getText().toString();
                String euros = convertCurrency(inputValue, Currency.euro);
                txtInputCredit.getEditText().setText(euros);
            }
        });
        btnCalculateHomeLoan.setOnClickListener(v -> {

            boolean allFieldsFill = !Objects.requireNonNull(txtInputCredit.getEditText()).getText().toString().isEmpty() && slideInputYear.getValue() > 0 && slideInterestRate.getValue() > 0;

            if (allFieldsFill) {

                double creditAmount = Float.parseFloat(Objects.requireNonNull(txtInputCredit.getEditText()).getText().toString());
                double interestRate = Float.parseFloat(String.valueOf(slideInterestRate.getValue())) / 100;
                double nbYearsOfRefundInMonth = (slideInputYear.getValue()) * 12;
                int monthlyRefund = (int) Math.round(((creditAmount * interestRate) / 12.0) / (1 - (Math.pow((1 + (interestRate / 12.0)), (-1 * nbYearsOfRefundInMonth)))));

                if (switchCurrency.isChecked()) {
                    monthlyRefund = (int) convertEuroToDollar(monthlyRefund);
                }
                resultMonthly.setVisibility(View.VISIBLE);
                if (switchCurrency.isChecked()) {
                    resultMonthly.setText(getResources().getString(R.string.refund_euros_by_month, monthlyRefund));
                } else {
                    resultMonthly.setText(getResources().getString(R.string.refund_dollars_by_month, monthlyRefund));
                }
            } else {
                showToast(this, getResources().getString(R.string.error_fill_all_field));
            }
        });

    }

    private void imageChooser(String value) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        if (value.equals(PICTURE_USER)) {
            launchGalleryPickPictureUser.launch(intent);
        } else {
            launchGalleryPickPictureRealEstate.launch(intent);
        }
    }

    ActivityResultLauncher<Intent> launchGalleryPickPictureUser
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
                            setProfilePicture(this, user.getPicture(), picture);
                        });
                    }
                }
            });

    ActivityResultLauncher<Intent> launchGalleryPickPictureRealEstate
            = registerForActivityResult(
            new ActivityResultContracts
                    .StartActivityForResult(),
            result -> {
                if (result.getResultCode()
                        == Activity.RESULT_OK) {
                    Intent data = result.getData();

                    if (data != null
                            && data.getData() != null) {
                        selectedImageUri = data.getData();
                        Image image = new Image(this.id, selectedImageUri.toString());
                        realEstateViewModel.addRealEstateImage(image);
                        showToast(this, getResources().getString(R.string.image_added));
                    }
                }
            });


    private void updateCustomDialogSettings(User user) {

        if (doesUserHaveAPicture(user)) {
            setProfilePicture(this, user.getPicture(), picture);
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
                setErrorOnField(customDialog, key, getResources().getString(R.string.field_is_requiered));
                isEmpty = true;
            } else {
                clearErrorOnField(customDialog, key);
            }
        }
        return isEmpty;
    }

    private boolean isUserEmailExistAlready(User user) {
        boolean isUserEmailExistYet = userViewModel.isUserEmailExistAlready(user);
        if (isUserEmailExistYet) {
            setErrorOnField(customDialog, EMAIL, getResources().getString(R.string.email_exists_already));
        }
        return isUserEmailExistYet;
    }

    private boolean emailValid() {

        boolean emailFormatValid = Patterns.EMAIL_ADDRESS.matcher(Objects.requireNonNull(maskFieldsSettings.get(EMAIL))).matches();
        if (!emailFormatValid) {
            setErrorOnField(customDialog, EMAIL, getResources().getString(R.string.email_invalid));
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
