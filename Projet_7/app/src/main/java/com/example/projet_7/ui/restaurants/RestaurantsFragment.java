package com.example.projet_7.ui.restaurants;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.projet_7.databinding.FragmentRestaurantsBinding;
import com.example.projet_7.model.Restaurant;
import com.example.projet_7.viewModel.RestaurantViewModel;

import java.util.ArrayList;


public class RestaurantsFragment extends Fragment {

    private FragmentRestaurantsBinding binding;
    private RestaurantViewModel restaurantViewModel;
    public ArrayList<Restaurant> restaurants = new ArrayList<>();

    private void initViewModel() {
        restaurantViewModel = new ViewModelProvider(requireActivity()).get(RestaurantViewModel.class);
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        initViewModel();
        binding = FragmentRestaurantsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initRecyclerView();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        restaurantViewModel.getLiveData().observe(getViewLifecycleOwner(), mRestaurants -> {
            restaurants.clear();
            restaurants.addAll(mRestaurants);
            binding.recyclerview.getAdapter().notifyDataSetChanged();
        });

    }

    private void initRecyclerView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.recyclerview.setLayoutManager(layoutManager);
        RestaurantAdapter mAdapter = new RestaurantAdapter(restaurants);


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.recyclerview.getContext(),
                layoutManager.getOrientation());
        binding.recyclerview.addItemDecoration(dividerItemDecoration);
        binding.recyclerview.setAdapter(mAdapter);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}