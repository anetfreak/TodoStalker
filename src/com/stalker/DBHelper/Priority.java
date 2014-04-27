package com.stalker.DBHelper;

public class Priority {

	private int id;
	private int priorityNum;
	private String priorityName;
	
	public Priority(){
	}
	
	public void setID(int id){
		this.id = id;
	}
	
	public void setPriorityNum(int priorityNum){
		this.priorityNum = priorityNum;
	}
	
	public void setPriorityName(String priorityName){
		this.priorityName = priorityName;
	}
	
	public int getID(){
		return this.id;
	}
	
	public int getPriorityNum(){
		return this.priorityNum;
	}
	
	public String getPriorityName(){
		return this.priorityName;
	}
}
