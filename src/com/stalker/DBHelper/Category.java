package com.stalker.DBHelper;

public class Category {
	private int id;
	private String categoryName;
	
	public Category(){
	}
	
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
		
	}

	public void setID(int id) {
		this.id = id;
	}
	
	public int getID(){
		return this.id;
	}
	
	public String getCategoryName(){
		return this.categoryName;
	}

	@Override
	public String toString(){
		return this.categoryName;
	}
}
