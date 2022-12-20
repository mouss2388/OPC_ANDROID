package com.openclassrooms.realestatemanager.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.database.model.Image;
import com.openclassrooms.realestatemanager.database.model.RealEstate;
import com.openclassrooms.realestatemanager.databinding.FragmentRealestatesDetailBinding;
import com.openclassrooms.realestatemanager.viewModel.RealEstateViewModel;

import java.util.List;

public class RealEstateDetailFragment extends Fragment {

    public FragmentRealestatesDetailBinding binding;
    public RealEstate realEstate;
    public List<Image> images;
    public RealEstateViewModel realEstateViewModel;

    public static RealEstateDetailFragment newInstance() {

        RealEstateDetailFragment realEstateDetailFragment = new RealEstateDetailFragment();
        realEstateDetailFragment.realEstate = null;
        realEstateDetailFragment.images = null;

        return realEstateDetailFragment;
    }


    private void initViewModel() {
        realEstateViewModel = new ViewModelProvider(this).get(RealEstateViewModel.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRealestatesDetailBinding.inflate(inflater, container, false);
        initViewModel();
        return binding.getRoot();
    }

    public void setRealEstate(RealEstate realEstate, List<Image> images) {
        this.realEstate = realEstate;
        this.images =images;

        binding.fragmentRealEstatesDetail.setVisibility(View.VISIBLE);

        binding.type.setText(String.valueOf(realEstate.getTypeRealEstate()));

        binding.description.setText(realEstate.getDescription());

        binding.surface.setText(String.valueOf(realEstate.getSurface()));

        binding.bathrooms.setText(String.valueOf(realEstate.getNbBathRoom()));

        binding.bedrooms.setText(String.valueOf(realEstate.getNbBedRoom()));

        binding.rooms.setText(String.valueOf(realEstate.getNbRoom()));

        binding.location.setText(realEstate.getAddress());
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (this.realEstate != null) {

            setRealEstate(realEstate,images);

        } else {

            binding.fragmentRealEstatesDetail.setVisibility(View.INVISIBLE);
        }
    }
}
