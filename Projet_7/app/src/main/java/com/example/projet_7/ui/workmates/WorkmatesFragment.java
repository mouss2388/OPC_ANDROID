package com.example.projet_7.ui.workmates;

import static com.example.projet_7.ui.MainActivity.placesClient;
import static com.example.projet_7.utils.Utils.isQuerySearchEmpty;
import static com.example.projet_7.utils.Utils.isSearchBoxLengthAtleast1;

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

import com.example.projet_7.databinding.FragmentWorkmatesBinding;
import com.example.projet_7.model.Restaurant;
import com.example.projet_7.model.User;
import com.example.projet_7.viewModel.RestaurantViewModel;
import com.example.projet_7.viewModel.WorkMateViewModel;

import java.util.ArrayList;


public class WorkmatesFragment extends Fragment {

    private FragmentWorkmatesBinding binding;
    private WorkMateViewModel workMateViewModel;
    private RestaurantViewModel restaurantViewModel;
    public ArrayList<User> workmates = new ArrayList<>();
    public ArrayList<User> workmatesCopy = new ArrayList<>();
    public ArrayList<Restaurant> restaurantsBooked = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentWorkmatesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initRecyclerView();
        return root;
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.recyclerview.setLayoutManager(layoutManager);
        WorkMateAdapter mAdapter = new WorkMateAdapter(workmates, restaurantsBooked);


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.recyclerview.getContext(),
                layoutManager.getOrientation());
        binding.recyclerview.addItemDecoration(dividerItemDecoration);
        binding.recyclerview.setAdapter(mAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.initViewModel();
        getUsersData();
        getWorkmatesByQuery();
    }

    private void initViewModel() {
        workMateViewModel = new ViewModelProvider(requireActivity()).get(WorkMateViewModel.class);
        restaurantViewModel = new ViewModelProvider(requireActivity()).get(RestaurantViewModel.class);
    }

    private void getUsersData() {

        workMateViewModel.getLiveData().observe(getViewLifecycleOwner(), mWorkmates -> {

            if (isQuerySearchEmpty(getContext())) {

                if (workmatesCopyNotExistsYet()) {
                    workmatesCopy.addAll(mWorkmates);
                }


                updateList(mWorkmates);
                restaurantViewModel.getLiveDataRestaurantBooked().observe(getViewLifecycleOwner(), restaurants -> {

                    restaurantsBooked.clear();
                    restaurantsBooked.addAll(restaurants);

                    binding.recyclerview.getAdapter().notifyDataSetChanged();
                });
                restaurantViewModel.getRestaurantsBooked(workmates, placesClient);
            }
        });
    }

    private boolean workmatesCopyNotExistsYet() {
        return workmatesCopy.isEmpty();
    }


    private void getWorkmatesByQuery() {

        workMateViewModel.getWorkmatesByQuery().observe(getViewLifecycleOwner(), query -> {

            if (isSearchBoxLengthAtleast1(getContext())) {

                ArrayList<User> workmatesFiltered = getWorkmatesFilterByQuery(query);
                updateList(workmatesFiltered);
            }
        });
    }

    private ArrayList<User> getWorkmatesFilterByQuery(String query) {

        ArrayList<User> usersMatched = new ArrayList<>();

        if (!workmatesCopy.isEmpty()) {
            for (User workmate : workmatesCopy) {
                String firstname = workmate.getUsername().split(" ")[0].toLowerCase();

                if (firstname.contains(query.toLowerCase())) {
                    usersMatched.add(workmate);
                }
            }
        }

        return usersMatched;
    }


    private void updateList(ArrayList<User> mWorkmates) {
        workmates.clear();
        workmates.addAll(mWorkmates);
        binding.recyclerview.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}