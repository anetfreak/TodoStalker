package com.stalker.DBHelper;

public class PreferredLocation {
	private int id;
	private int categ_ID;
	private String locationName;
	
	public PreferredLocation(){
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCateg_ID() {
		return categ_ID;
	}

	public void setCateg_ID(int categ_ID) {
		this.categ_ID = categ_ID;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	
	@Override
	public String toString(){
		return this.locationName;
	}
}
