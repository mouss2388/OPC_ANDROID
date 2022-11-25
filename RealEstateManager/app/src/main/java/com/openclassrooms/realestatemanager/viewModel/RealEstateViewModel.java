package com.openclassrooms.realestatemanager.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.database.model.RealEstate;
import com.openclassrooms.realestatemanager.repository.RealEstateRepository;

import java.util.List;

public class RealEstateViewModel extends AndroidViewModel {

    private final RealEstateRepository repository;

    private final LiveData<List<RealEstate>> allRealEstates;

    public RealEstateViewModel(Application application) {
        super(application);
        repository = new RealEstateRepository(application);
        allRealEstates = repository.getAllRealEstates();
    }


    public long insert(RealEstate realEstate) {
        return repository.insert(realEstate);
    }

    public int update(RealEstate realEstate){
        return repository.update(realEstate);
    }

    public LiveData<RealEstate> getRealEstateById(long id) {
        return repository.getRealEstateById(id);
    }
    public LiveData<List<RealEstate>> getAllRealEstates() {
        return allRealEstates;
    }
}