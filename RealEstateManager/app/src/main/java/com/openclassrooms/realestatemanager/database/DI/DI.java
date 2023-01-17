package com.openclassrooms.realestatemanager.database.DI;


import com.openclassrooms.realestatemanager.database.service.image.DummyImageApiService;
import com.openclassrooms.realestatemanager.database.service.image.ImageApiService;
import com.openclassrooms.realestatemanager.database.service.realEstate.DummyRealEstateApiService;
import com.openclassrooms.realestatemanager.database.service.realEstate.RealEstateApiService;
import com.openclassrooms.realestatemanager.database.service.user.DummyUserApiService;
import com.openclassrooms.realestatemanager.database.service.user.UserApiService;

/**
 * Dependency injector to get instance of services
 */
@SuppressWarnings("unused")
public class DI {

    private static final RealEstateApiService serviceRealEstate = new DummyRealEstateApiService();
    private static final UserApiService serviceUser = new DummyUserApiService();
    private static final ImageApiService serviceImage = new DummyImageApiService();

    /**
     * Get an instance on @{@link RealEstateApiService}
     *
     * @return serviceRealEstate
     */
    public static RealEstateApiService getRealEstatesApiService() {
        return serviceRealEstate;
    }

    /**
     * Get an instance on @{@link UserApiService}
     *
     * @return serviceUser
     */
    public static UserApiService getUserApiService() {
        return serviceUser;
    }

    /**
     * Get an instance on @{@link ImageApiService}
     *
     * @return serviceImage
     */
    public static ImageApiService getImageApiService() {
        return serviceImage;
    }

    /**
     * Get always a new instance on @{@link RealEstateApiService}. Useful for tests, so we ensure the context is clean.
     *
     * @return DummyRealEstateApiService
     */
    public static RealEstateApiService getNewInstanceRealEstateApiService() {
        return new DummyRealEstateApiService();
    }

    /**
     * Get always a new instance on @{@link RealEstateApiService}. Useful for tests, so we ensure the context is clean.
     *
     * @return DummyUserApiService
     */
    public static UserApiService getNewInstanceUserApiService() {
        return new DummyUserApiService();
    }

    /**
     * Get always a new instance on @{@link ImageApiService}. Useful for tests, so we ensure the context is clean.
     *
     * @return DummyUserApiService
     */
    public static ImageApiService getNewInstanceImageApiService() {
        return new DummyImageApiService();
    }
}
