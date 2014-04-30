package com.stalker.DBHelper;

public class Stores {
	private int id;
	private String storeName;
	
	public Stores(){
	}
	
	public void setID(int id){
		this.id = id;
	}
	
	public void setStoreName(String storeName){
		this.storeName = storeName;
	}
	
	public int getID(){
		return this.id;
	}
	
	public String getStoreName(){
		return this.getStoreName();
	}
	
	@Override
	public String toString(){
		return this.storeName;
		
	}
	
}
