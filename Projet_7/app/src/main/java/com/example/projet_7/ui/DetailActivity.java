package com.example.projet_7.ui;

import static com.example.projet_7.utils.Utils.convertPurcentageToRating;
import static com.example.projet_7.utils.Utils.convertRatingToPurcentage;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
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
import com.example.projet_7.ui.adapter.DetailWorkmateAdapter;
import com.example.projet_7.viewModel.RestaurantViewModel;
import com.example.projet_7.viewModel.WorkMateViewModel;

import java.util.ArrayList;

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

        getIdRestaurant();
        initRecyclerView();

    }


    private void getIdRestaurant() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            restaurantId = bundle.getString("id_restaurant");
            initViewModel();
        }
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
        restaurantViewModel.getLiveDataDetail().observe(this, this::bindValues);
        restaurantViewModel.getRestaurantDetail(MainActivity.placesClient, restaurantId);

        workMateViewModel = new ViewModelProvider(this).get(WorkMateViewModel.class);

        updateRecyclerView();
    }

    private void bindValues(Restaurant restaurant) {
        //TODO CONTINUE TO BIND VALUE WITH LAYOUT VIEW
        binding.nameRestaurant.setText(restaurant.getName());
        binding.addressRestaurant.setText(restaurant.getAddress());
        double purcentageRating = convertRatingToPurcentage(restaurant.getRating());
        float rating = convertPurcentageToRating(purcentageRating);
        if (rating == 0) {
            binding.starsRatingRestaurant.setVisibility(View.GONE);
        } else {
            binding.starsRatingRestaurant.setRating(rating);
        }

        setBookFloatBUtton(restaurant);
        initListeners(restaurant);
    }

    private void setBookFloatBUtton(Restaurant restaurant) {
        userManager.getUserData().addOnCompleteListener(task -> {
            User user = task.getResult();
            if (user.getRestaurantBookedId().isEmpty() || !user.getRestaurantBookedId().equals(restaurant.getId())) {
                binding.addFavorite.setImageResource(R.drawable.ic_check_circle_outline_24);
            } else {
                binding.addFavorite.setImageResource(R.drawable.ic_check_circle_24);
            }

        });
    }

    private void initListeners(Restaurant restaurant) {
        if (restaurant.getPhoto() != null) {
            Drawable photo = new BitmapDrawable(this.getResources(), restaurant.getPhoto());
            binding.restaurantImg.setBackground(photo);
        }
        if (restaurant.getPhoneNumber() != null) {

            binding.call.setOnClickListener(v -> Toast.makeText(DetailActivity.this, restaurant.getPhoneNumber(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "NONE PHONE_NUMBER", Toast.LENGTH_SHORT).show();

        }
        if (restaurant.getWebsite() != null) {

            binding.website.setOnClickListener(v -> Toast.makeText(DetailActivity.this, restaurant.getWebsite().toString(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "NONE WEB_URI", Toast.LENGTH_SHORT).show();
        }


        binding.like.setOnClickListener(v -> userManager.getUserData().addOnCompleteListener(task -> {
            User user = task.getResult();
            if (user.getRestaurantBookedId().isEmpty() || !user.getRestaurantBookedId().equals(restaurant.getId())) {
                binding.addFavorite.setImageResource(R.drawable.ic_check_circle_24);
                user.setRestaurantBookedId(restaurant.getId());
                userManager.updateUserData(user);
                Toast.makeText(this, "booked", Toast.LENGTH_SHORT).show();
                updateRecyclerView();
            } else {
                binding.addFavorite.setImageResource(R.drawable.ic_check_circle_outline_24);
                user.setRestaurantBookedId("");
                userManager.updateUserData(user);
                Toast.makeText(this, "unbooked", Toast.LENGTH_SHORT).show();

                updateRecyclerView();
            }
        }));

    }

    private void updateRecyclerView() {
        //TODO Create getWorkmatesWhichBooked(String restaurantId){}
        workMateViewModel.getLiveDataRestaurantBooked().observe(this, workmates -> {

            users.clear();
            users.addAll(workmates);
            binding.recyclerviewWorkMates.getAdapter().notifyDataSetChanged();
        });
        workMateViewModel.getWorkmatesBookedRestaurant(restaurantId);
    }
}