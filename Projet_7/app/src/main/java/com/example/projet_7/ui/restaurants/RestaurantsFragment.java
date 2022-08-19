package com.example.projet_7.ui.restaurants;

import static com.example.projet_7.utils.Utils.getDistanceBetweenLocationAndRestaurant;
import static com.example.projet_7.utils.Utils.getLatLngForMatrixApi;

import android.location.Location;
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
import com.example.projet_7.model.matrix_api.ElementsItem;
import com.example.projet_7.model.matrix_api.RowsItem;
import com.example.projet_7.ui.MainActivity;
import com.example.projet_7.utils.OnMatrixApiListReceivedCallback;
import com.example.projet_7.viewModel.RestaurantViewModel;

import java.util.ArrayList;
import java.util.List;


public class RestaurantsFragment extends Fragment implements OnMatrixApiListReceivedCallback {

    private FragmentRestaurantsBinding binding;
    private RestaurantViewModel restaurantViewModel;
    private ArrayList<Restaurant> restaurants = new ArrayList<>();

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        restaurantViewModel.getLiveData().observe(getViewLifecycleOwner(), mRestaurants -> {
            restaurants.clear();
            restaurants.addAll(mRestaurants);
            calculDistFromLocationToRestaurants();

        });
    }

    private void calculDistFromLocationToRestaurants() {

        Location currentLocation = ((MainActivity) requireContext()).currentLocation;
        for (int idx = 0; idx < restaurants.size(); idx++) {
            StringBuilder currentLocationCoord = getLatLngForMatrixApi(currentLocation);
            StringBuilder destinationCoord = getLatLngForMatrixApi(restaurants.get(idx).getLatLng());
            getDistanceBetweenLocationAndRestaurant(this, getContext(), currentLocationCoord, destinationCoord, idx);
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onMatrixApiListReceivedCallback(List<RowsItem> rowsItem, int idx) {
        ElementsItem matrixItem = rowsItem.get(0).getElements().get(0);
        String distance = matrixItem.getDistance().getText();
        String duration = matrixItem.getDuration().getText();
        restaurants.get(idx).setDistance(distance);
        restaurants.get(idx).setDuration(duration);

        if (idx == restaurants.size() - 1) {
            binding.recyclerview.getAdapter().notifyDataSetChanged();
        }

    }
}