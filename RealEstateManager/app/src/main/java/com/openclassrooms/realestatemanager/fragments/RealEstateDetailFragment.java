package com.openclassrooms.realestatemanager.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.databinding.FragmentRealestatesDetailBinding;
import com.openclassrooms.realestatemanager.viewModel.RealEstateViewModel;

public class RealEstateDetailFragment extends Fragment {

    public FragmentRealestatesDetailBinding binding;

    public RealEstateViewModel realEstateViewModel;

    public static RealEstateDetailFragment newInstance() {
        return (new RealEstateDetailFragment());
    }


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRealestatesDetailBinding.inflate(inflater, container, false);
        this.initViewModel();

//        realEstateViewModel.getRealEstatesImg()
        return binding.getRoot();
    }

    public void initViewModel() {
        realEstateViewModel = new ViewModelProvider(requireActivity()).get(RealEstateViewModel.class);
    }
}
