package com.openclassrooms.realestatemanager.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentRealestatesBinding;

public class RealEstateFragment extends Fragment {

    private RealEstateListFragment realEstateListFragment;
    private RealEstateDetailFragment realEstateDetailFragment;
    public FragmentRealestatesBinding binding;


    public static RealEstateFragment newInstance() {
        return (new RealEstateFragment());
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.configRealEstateListAndShow();
        this.configRealEstateDetailAndShow();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRealestatesBinding.inflate(inflater, container, false);
        return binding.getRoot();
//        return inflater.inflate(R.layout.fragment_realestates, container, false);
    }

    private void configRealEstateListAndShow() {

        if (realEstateListFragment == null) {
            realEstateListFragment = new RealEstateListFragment();

            requireActivity().getSupportFragmentManager().beginTransaction().add(R.id.real_estates_list_frame_layout, realEstateListFragment).commit();
        }

    }

    private void configRealEstateDetailAndShow() {
        if (realEstateDetailFragment == null) {
            realEstateDetailFragment = new RealEstateDetailFragment();

            requireActivity().getSupportFragmentManager().beginTransaction().add(R.id.real_estates_detail_frame_layout, realEstateDetailFragment).commit();
        }
    }

//    private void showFirstFragment() {
//        Fragment visibleFragment = requireActivity().getSupportFragmentManager().findFragmentById(R.id.main_Layout);
//        if (visibleFragment == null) {
//            // 1.1 - Show News Fragment
//            this.showFrag(R.id.fragment_real_estates_list);
//            this.showFrag(R.id.fragment_real_estates_detail);
//        }
//    }
//
//    private void showFrag(int fragId) {
//
//        if (fragId == R.id.fragment_real_estates_list) {
//            this.mFragment = RealEstateListFragment.newInstance();
//            this.startTransactionFragment(this.mFragment);
//        }else{
//            this.mFragment2 = RealEstateDetailFragment.newInstance();
//            this.startTransactionFragment(this.mFragment2);
//        }
//    }
//
//    // 3 - Generic method that will replace and show a fragment inside the MainActivity Frame Layout
//    private void startTransactionFragment(Fragment fragment) {
//        if (!fragment.isVisible()) {
//            requireActivity().getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.main_Layout, fragment).commit();
//        }
//    }

}
