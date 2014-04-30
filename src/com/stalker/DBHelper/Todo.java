package com.stalker.DBHelper;

public class Todo {

	private int id;
	private String note;
	private String category;
	private String prefLoc;
	private String startDate;
	private String endDate;
	private int status;
	
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
	
	public void setStatus(int status){
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

	public int getStatus() {
		return status;
	}
	
}
