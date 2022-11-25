package com.openclassrooms.realestatemanager.database.DI;


import com.openclassrooms.realestatemanager.database.service.DummyRealEstateApiService;
import com.openclassrooms.realestatemanager.database.service.RealEstateApiService;

/**
 * Dependency injector to get instance of services
 */
public class DI {

    private static final RealEstateApiService service = new DummyRealEstateApiService();

    /**
     * Get an instance on @{@link RealEstateApiService}
     *
     * @return
     */
    public static RealEstateApiService getRealEstatesApiService() {
        return service;
    }

    /**
     * Get always a new instance on @{@link RealEstateApiService}. Useful for tests, so we ensure the context is clean.
     *
     * @return
     */
    public static RealEstateApiService getNewInstanceApiService() {
        return new DummyRealEstateApiService();
    }
}
