package com.openclassrooms.realestatemanager.database.model.geocoding_api;

import java.util.List;

public class Response{
	private List<ResultsItem> results;
	private String status;

	public List<ResultsItem> getResults(){
		return results;
	}

	public String getStatus(){
		return status;
	}
}