package com.stalker.DBHelper;

import java.io.Serializable;

public class Todo implements Serializable {

	private static final long serialVersionUID = -5250921033443809685L;
	
	private int id;
	private String note;
	private String category;
	private String prefLoc;
	private String startDate;
	private String endDate;
	private String status;
	
	public Todo(){
	}
	
	public void setID(int id){
		this.id = id;
	}
	
	public void setNote(String note){
		this.note = note;
	}
	
	public void setCategory(String category){
		this.category = category;
	}
	
	public void setPrefLoc(String prefLoc){
		this.prefLoc = prefLoc;
	}
	
	public void setStartDate(String startDate){
		this.startDate = startDate;
	}
	
	public void setEndDate(String endDate){
		this.endDate = endDate;
	}
	
	public void setStatus(String status){
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public String getNote() {
		return note;
	}

	public String getCategory() {
		return category;
	}

	public String getPrefLoc() {
		return prefLoc;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public String getStatus() {
		return status;
	}
	
}
