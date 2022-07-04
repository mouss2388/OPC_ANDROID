package com.example.projet_7.ui.restaurants;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.projet_7.databinding.FragmentRestaurantsBinding;


public class RestaurantsFragment extends Fragment {

    private FragmentRestaurantsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        DashboardViewModel dashboardViewModel =
//                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentRestaurantsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textRestaurants;
//        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}