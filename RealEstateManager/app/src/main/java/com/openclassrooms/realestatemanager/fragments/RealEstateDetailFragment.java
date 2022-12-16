package com.openclassrooms.realestatemanager.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.databinding.FragmentRealestatesDetailBinding;
import com.openclassrooms.realestatemanager.viewModel.RealEstateViewModel;

public class RealEstateDetailFragment extends Fragment {

    public FragmentRealestatesDetailBinding binding;
    public long id;
    public RealEstateViewModel realEstateViewModel;

    public static RealEstateDetailFragment newInstance() {

        RealEstateDetailFragment realEstateDetailFragment = new RealEstateDetailFragment();
        realEstateDetailFragment.id = 0;
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

    public void setRealEstate(long id) {
        this.id = id;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (this.id > 0) {
            realEstateViewModel.getRealEstateById(id).observe(
                    getViewLifecycleOwner(), realEstate -> {
                        binding.textView.setText(realEstate.getName());
                    });
        }
    }
}
