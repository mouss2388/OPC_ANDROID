package com.openclassrooms.realestatemanager.database.model.geocoding_api;

public class Viewport{
	private Southwest southwest;
	private Northeast northeast;

	public Southwest getSouthwest(){
		return southwest;
	}

	public Northeast getNortheast(){
		return northeast;
	}
}
