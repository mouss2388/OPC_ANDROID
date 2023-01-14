package com.openclassrooms.realestatemanager.database.model.geocoding_api;

import java.util.List;

public class AddressComponentsItem{
	private List<String> types;
	private String shortName;
	private String longName;

	public List<String> getTypes(){
		return types;
	}

	public String getShortName(){
		return shortName;
	}

	public String getLongName(){
		return longName;
	}
}