package com.openclassrooms.realestatemanager.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.openclassrooms.realestatemanager.databinding.FragmentRealestatesDetailBinding;

public class RealEstateDetailFragment extends Fragment {

    public FragmentRealestatesDetailBinding binding;

    public static RealEstateDetailFragment newInstance() {
        return (new RealEstateDetailFragment());
    }



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRealestatesDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}