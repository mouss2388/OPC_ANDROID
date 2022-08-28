package com.example.projet_7.ui;

import static com.example.projet_7.utils.Utils.RESTAURANT_ID;
import static com.example.projet_7.utils.Utils.convertPurcentageToRating;
import static com.example.projet_7.utils.Utils.convertRatingToPurcentage;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.projet_7.R;
import com.example.projet_7.databinding.ActivityDetailBinding;
import com.example.projet_7.manager.UserManager;
import com.example.projet_7.model.Restaurant;
import com.example.projet_7.model.User;
import com.example.projet_7.ui.adapters.DetailWorkmateAdapter;
import com.example.projet_7.viewModel.RestaurantViewModel;
import com.example.projet_7.viewModel.WorkMateViewModel;

import java.util.ArrayList;
import java.util.Objects;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;
    private WorkMateViewModel workMateViewModel;
    private final UserManager userManager = UserManager.getInstance();

    private final ArrayList<User> users = new ArrayList<>();

    private String restaurantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        initRecyclerView();
        restaurantId = getIdRestaurant();
        if(!restaurantId.isEmpty()){
            initViewModel();
        }

    }


    private String getIdRestaurant() {
        Bundle bundle = getIntent().getExtras();
        return bundle.getString(RESTAURANT_ID);
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerviewWorkMates.setLayoutManager(layoutManager);

        DetailWorkmateAdapter detailWorkmateAdapter = new DetailWorkmateAdapter(users);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.recyclerviewWorkMates.getContext(),
                layoutManager.getOrientation());
        binding.recyclerviewWorkMates.addItemDecoration(dividerItemDecoration);
        binding.recyclerviewWorkMates.setAdapter(detailWorkmateAdapter);
    }

    private void initViewModel() {


        RestaurantViewModel restaurantViewModel = new ViewModelProvider(this).get(RestaurantViewModel.class);
        workMateViewModel = new ViewModelProvider(this).get(WorkMateViewModel.class);

        restaurantViewModel.getLiveDataRestaurantDetail().observe(this, this::bindValuesRestaurant);
        restaurantViewModel.getDetailsRestaurant(MainActivity.placesClient, restaurantId);
    }

    private void bindValuesRestaurant(Restaurant restaurant) {

        binding.nameRestaurant.setText(restaurant.getName());
        binding.addressRestaurant.setText(restaurant.getAddress());

        double percentageRating = convertRatingToPurcentage(restaurant.getRating());
        float rating = convertPurcentageToRating(percentageRating);

        if (rating == 0) {
            binding.starsRatingRestaurant.setVisibility(View.GONE);
        } else {
            binding.starsRatingRestaurant.setRating(rating);
        }

        updateRecyclerView();
        setBookFloatButton(restaurant);
        initListeners(restaurant);
    }

    private void setBookFloatButton(Restaurant restaurant) {

        userManager.getUserData().addOnCompleteListener(task -> {
            User user = task.getResult();

            if (userBookedNoOneRestaurant(user) || userNotBookedThisRestaurant(user, restaurant)) {
                binding.addFavorite.setImageResource(R.drawable.ic_check_circle_outline_24);
            } else {
                binding.addFavorite.setImageResource(R.drawable.ic_check_circle_24);
            }
        });
    }

    private boolean userBookedNoOneRestaurant(User user) {
        return user.getRestaurantBookedId().isEmpty();
    }

    private boolean userNotBookedThisRestaurant(User user, Restaurant restaurant) {
        return !Objects.equals(user.getRestaurantBookedId(), restaurant.getId());

    }

    private void initListeners(Restaurant restaurant) {
        if (restaurant.getPhoto() != null) {
            Drawable photo = new BitmapDrawable(this.getResources(), restaurant.getPhoto());
            binding.restaurantImg.setBackground(photo);
        }
        binding.call.setOnClickListener(v -> setupListener(restaurant, binding.call));
        binding.website.setOnClickListener(v -> setupListener(restaurant, binding.website));
        binding.like.setOnClickListener(v -> setupListener(restaurant, binding.like));
    }

    private void setupListener(Restaurant restaurant, TextView view) {

        if (view.getId() == binding.call.getId()) {

            if (restaurant.getPhoneNumber() == null)
                Toast.makeText(this, getString(R.string.none_phone_number_found), Toast.LENGTH_SHORT).show();

            else {
                call(restaurant);
            }

        } else if (view.getId() == binding.website.getId()) {


            if (restaurant.getWebsite() == null)
                Toast.makeText(this, getString(R.string.none_web_site_found), Toast.LENGTH_SHORT).show();
            else {
                consultWebSite(restaurant);
            }

        } else {

            getUserInfo(restaurant);
        }
    }

    private void call(Restaurant restaurant) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", restaurant.getPhoneNumber(), null));
        startActivity(intent);
    }

    private void consultWebSite(Restaurant restaurant) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, restaurant.getWebsite());
        startActivity(browserIntent);
    }


    private void getUserInfo(Restaurant restaurant) {
        userManager.getUserData().addOnCompleteListener(task -> {
            User user = task.getResult();
            gottaIamReserve(restaurant, user, userBookedNoOneRestaurant(user) || userNotBookedThisRestaurant(user, restaurant));

        });
    }


    private void gottaIamReserve(Restaurant restaurant, User user, boolean reserve) {

        if (reserve) {
            binding.addFavorite.setImageResource(R.drawable.ic_check_circle_24);
            user.setRestaurantBookedId(restaurant.getId());
            Toast.makeText(this, getString(R.string.booked), Toast.LENGTH_SHORT).show();
        } else {

            binding.addFavorite.setImageResource(R.drawable.ic_check_circle_outline_24);
            user.setRestaurantBookedId("");
            Toast.makeText(this, getString(R.string.unbooked), Toast.LENGTH_SHORT).show();
        }

        userManager.updateUserData(user);
    }


    private void updateRecyclerView() {

        workMateViewModel.getLiveDataRestaurantBooked().observe(this, workmates -> {

            users.clear();
            users.addAll(workmates);
            binding.recyclerviewWorkMates.getAdapter().notifyDataSetChanged();
        });
        workMateViewModel.getWorkmatesBookedRestaurant(restaurantId);
    }
}