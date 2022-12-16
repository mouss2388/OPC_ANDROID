package com.openclassrooms.realestatemanager.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.openclassrooms.realestatemanager.adapters.RealEstateAdapter;
import com.openclassrooms.realestatemanager.database.model.RealEstate;
import com.openclassrooms.realestatemanager.databinding.FragmentRealestatesListBinding;

import java.util.List;
import java.util.Objects;

public class RealEstateListFragment extends Fragment implements RealEstateAdapter.OnRealEstateListener {

    public FragmentRealestatesListBinding binding;
    private List<RealEstate> mRealEstates;
    public  RealEstateAdapter.OnRealEstateListener onRealEstateListener;


    public static RealEstateListFragment newInstance(List<RealEstate> realEstates, RealEstateAdapter.OnRealEstateListener onRealEstateListener) {

        RealEstateListFragment realEstateListFragment = new RealEstateListFragment();
        realEstateListFragment.mRealEstates = realEstates;
        realEstateListFragment.onRealEstateListener = onRealEstateListener;
        return realEstateListFragment;
    }


    public void updateList(List<RealEstate> realEstates) {
        mRealEstates.clear();
        mRealEstates.addAll(realEstates);
        Objects.requireNonNull(binding.recyclerview.getAdapter()).notifyDataSetChanged();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRealestatesListBinding.inflate(inflater, container, false);
        initRecyclerView();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    }


    private void initRecyclerView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.recyclerview.setLayoutManager(layoutManager);
        RealEstateAdapter mAdapter = new RealEstateAdapter(mRealEstates, this);


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.recyclerview.getContext(),
                layoutManager.getOrientation());
        binding.recyclerview.addItemDecoration(dividerItemDecoration);
        binding.recyclerview.setAdapter(mAdapter);
    }

    @Override
    public void onRealEstateClick(long id) {
        Toast.makeText(getContext(), "ID FROM FRAGMENT " + id, Toast.LENGTH_SHORT).show();
        onRealEstateListener.onRealEstateClick(id);
    }
}
