package com.example.projet_7.ui.workmates;

import android.os.Bundle;
import android.util.Log;
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
import com.example.projet_7.model.User;
import com.example.projet_7.viewModel.WorkMateViewModel;

import java.util.ArrayList;


public class WorkmatesFragment extends Fragment {

    private FragmentWorkmatesBinding binding;
    private WorkMateViewModel workMateViewModel;
    public ArrayList<User> workmates = new ArrayList<>();


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
        WorkMateAdapter mAdapter = new WorkMateAdapter(workmates);


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

    }

    private void initViewModel() {
        workMateViewModel = new ViewModelProvider(requireActivity()).get(WorkMateViewModel.class);
    }

    private void getUsersData() {

        workMateViewModel.getLiveData().observe(getViewLifecycleOwner(), mUsers -> {
            for (int i = 0; i < mUsers.size(); i++) {
                Log.i("From repository", "username: " + mUsers.get(i).getUsername());
            }
            workmates.clear();
            workmates.addAll(mUsers);
            binding.recyclerview.getAdapter().notifyDataSetChanged();

        });
        //TODO move that in maps Fragment
        workMateViewModel.getWorkMates();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}