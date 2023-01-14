package com.openclassrooms.realestatemanager.database.model.geocoding_api;

public class Geometry{
	private Viewport viewport;
	private Location location;
	private String locationType;

	public Viewport getViewport(){
		return viewport;
	}

	public Location getLocation(){
		return location;
	}

	public String getLocationType(){
		return locationType;
	}
}
