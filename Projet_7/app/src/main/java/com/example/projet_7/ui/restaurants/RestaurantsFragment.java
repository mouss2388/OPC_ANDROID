package com.example.projet_7.ui.restaurants;

import static com.example.projet_7.utils.Utils.getDistanceBetweenLocationAndRestaurant;
import static com.example.projet_7.utils.Utils.getLatLngForMatrixApi;
import static com.example.projet_7.utils.Utils.isSearchBoxLengthAtleast3;

import android.annotation.SuppressLint;
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
import com.example.projet_7.ui.adapters.RestaurantAdapter;
import com.example.projet_7.utils.OnMatrixApiListReceivedCallback;
import com.example.projet_7.viewModel.RestaurantViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressLint("NotifyDataSetChanged")
public class RestaurantsFragment extends Fragment implements OnMatrixApiListReceivedCallback {

    private FragmentRestaurantsBinding binding;
    private RestaurantViewModel restaurantViewModel;
    private final ArrayList<Restaurant> restaurants = new ArrayList<>();
    private Location currentLocation;

    private void initViewModel() {
        restaurantViewModel = new ViewModelProvider(requireActivity()).get(RestaurantViewModel.class);
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        initViewModel();
        binding = FragmentRestaurantsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        currentLocation = ((MainActivity) requireContext()).currentLocation;

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
        getRestaurantsByPrediction();
        getRestaurantsAroundMe();
    }

    private void getRestaurantsByPrediction() {
        restaurantViewModel.getLiveDataRestaurantsByPrediction().observe(getViewLifecycleOwner(), mRestaurants -> {

            if (isSearchBoxLengthAtleast3(getContext())) {
                updateList(mRestaurants);
            }
        });
    }


    private void getRestaurantsAroundMe() {
        restaurantViewModel.getLiveDataRestaurantsAroundMe().observe(getViewLifecycleOwner(), mRestaurants -> {
            if (!isSearchBoxLengthAtleast3(getContext())) {
                updateList(mRestaurants);
            }
        });
    }

    private void updateList(ArrayList<Restaurant> mRestaurants) {
        restaurants.clear();
        restaurants.addAll(mRestaurants);
        Objects.requireNonNull(binding.recyclerview.getAdapter()).notifyDataSetChanged();
        calculateDistFromLocationToRestaurants();
    }

    private void calculateDistFromLocationToRestaurants() {

        for (int idx = 0; idx < restaurants.size(); idx++) {
            StringBuilder currentLocationCoord = getLatLngForMatrixApi(currentLocation);
            StringBuilder destinationCoord = getLatLngForMatrixApi(restaurants.get(idx).getLatLng());
            getDistanceBetweenLocationAndRestaurant(this, getContext(), currentLocationCoord, destinationCoord, restaurants.get(idx).getId());
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onMatrixApiListReceivedCallback(List<RowsItem> rowsItem, String id) {
        ElementsItem matrixItem = rowsItem.get(0).getElements().get(0);
        String distance = matrixItem.getDistance().getText();
        String duration = matrixItem.getDuration().getText();
        for (int idx = 0; idx < restaurants.size(); idx++) {
            if (restaurants.get(idx).getId().equals(id)) {
                restaurants.get(idx).setDistance(distance);
                restaurants.get(idx).setDuration(duration);

                if (idx == restaurants.size() - 1) {
                    Objects.requireNonNull(binding.recyclerview.getAdapter()).notifyDataSetChanged();
                }
                break;
            }

        }
    }
}