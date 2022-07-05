package com.example.projet_7.ui;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.projet_7.R;
import com.example.projet_7.databinding.ActivityMainBinding;
import com.example.projet_7.manager.UserManager;
import com.example.projet_7.ui.maps.MapsFragment;
import com.example.projet_7.ui.restaurants.RestaurantsFragment;
import com.example.projet_7.ui.workmates.WorkmatesFragment;
import com.example.projet_7.utils.Utils;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding binding;
    private UserManager userManager = UserManager.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        this.showSnackBarLogin();
        this.configureMenu();
        this.configureBottomNav();
        this.updateMenuWithUserData();

    }

    private void showSnackBarLogin() {
        Utils.showSnackBar(binding.mainLayout, getString(R.string.snackbar_msg_login_success));
    }

    private void configureMenu() {

        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();
    }


    private void configureToolBar() {
        binding.activityMainToolbar.setTitle(R.string.app_title);
        setSupportActionBar(binding.activityMainToolbar);
    }

    private void configureDrawerLayout() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.mainLayout, binding.activityMainToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.mainLayout.addDrawerListener(toggle);
        toggle.syncState();
    }


    // 3 - Configure NavigationView
    private void configureNavigationView() {
        binding.activityMainNavView.setNavigationItemSelectedListener(this);
    }


    private void configureBottomNav() {
        this.showFirstFrag();
        this.setupListernerBottomNav();
    }

    private void showFirstFrag() {
        replaceFragment(new MapsFragment());
    }

    private void setupListernerBottomNav() {

        binding.bottomNavView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_maps:
                    replaceFragment(new MapsFragment());
                    break;
                case R.id.navigation_restaurants:
                    replaceFragment(new RestaurantsFragment());
                    break;
                case R.id.navigation_workmates:
                    replaceFragment(new WorkmatesFragment());
                    break;
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.activity_main_frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_Item_1) {
            Toast.makeText(this, "menu_Item_1", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.menu_Item_2) {
            Dialog dialog = configDialogSetting();
            setupListenerDialogSettings(dialog);

        } else if (id == R.id.menu_Item_3) {
            userManager.signOut(this).addOnSuccessListener(aVoid -> {
                finish();
                Toast.makeText(this, getString(R.string.msg_you_are_log_out), Toast.LENGTH_SHORT).show();
            });
        }

        binding.mainLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    private Dialog configDialogSetting(){
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.settings_layout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return dialog;
    }

    private void setupListenerDialogSettings(Dialog dialog){

        dialog.show();
        ImageButton close = dialog.findViewById(R.id.close_Settings);
        Button deleteAccount = dialog.findViewById(R.id.remove_Account);

        deleteAccount.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setMessage(R.string.popup_message_confirmation_delete_account)
                .setPositiveButton(R.string.popup_message_choice_yes, (dialogInterface, i) ->
                        userManager.deleteUser(MainActivity.this)
                                .addOnSuccessListener(aVoid -> {
                                            dialog.dismiss();
                                            finish();
                                            Toast.makeText(this, getString(R.string.msg_acount_is_removed), Toast.LENGTH_SHORT).show();

                                        }
                                )
                )
                .setNegativeButton(R.string.popup_message_choice_no, null)
                .show());

        close.setOnClickListener(v -> dialog.dismiss());
    }

    private void updateMenuWithUserData() {

        if (userManager.isCurrentUserLogged()) {

            FirebaseUser user = userManager.getCurrentUser();
            if (user.getPhotoUrl() != null) {
                setProfilePicture(user.getPhotoUrl());
            }
            setTextUserData(user);
        }
    }

    private void setProfilePicture(Uri profilePictureUrl) {

        Glide.with(this)
                .load(profilePictureUrl)
                .apply(RequestOptions.circleCropTransform())
                .into((ImageView) accessMenuHeaderInfo().findViewById(R.id.user_Picture));
    }

    private View accessMenuHeaderInfo() {
        return binding.activityMainNavView.getHeaderView(0);
    }

    private void setTextUserData(FirebaseUser user) {

        //Get email & username from User
        String userName = TextUtils.isEmpty(user.getDisplayName()) ? getString(R.string.info_no_username_found) : user.getDisplayName();
        String userEmail = TextUtils.isEmpty(user.getEmail()) ? getString(R.string.info_no_email_found) : user.getEmail();

        //Update views with data
        TextView userNameTv = accessMenuHeaderInfo().findViewById(R.id.user_Name);
        TextView userEmailTv = accessMenuHeaderInfo().findViewById(R.id.user_Email);
        userNameTv.setText(userName);
        userEmailTv.setText(userEmail);
    }

    @Override
    public void onBackPressed() {
        // 5 - Handle back click to close menu
        if (binding.mainLayout.isDrawerOpen(GravityCompat.START)) {
            binding.mainLayout.closeDrawer(GravityCompat.START);
        } else {
            Utils.showSnackBar(binding.mainLayout, getString(R.string.snackbar_log_out));
        }
    }

}