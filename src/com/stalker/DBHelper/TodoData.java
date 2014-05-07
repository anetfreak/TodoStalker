package com.stalker.DBHelper;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class TodoData {

	private SQLiteDatabase db;
	private DatabaseHandler dbHandler;
	
	public TodoData(Context context){
		dbHandler = new DatabaseHandler(context);
	}
	
	public void openDB() throws SQLException{
		db = dbHandler.getWritableDatabase();
	}
	
	public void closeDB(){
		dbHandler.close();
	}
	
	public void createTodo(String note, String category, String subCategory, String startDate,
			 int status){
		ContentValues val = new ContentValues();
		val.put(DatabaseHandler.KEY_NOTE, note);
		val.put(DatabaseHandler.KEY_CATEGORY, category);
		val.put(DatabaseHandler.KEY_SUBCATEGORYTODO, subCategory);
		val.put(DatabaseHandler.KEY_STARTDATE, startDate);
		val.put(DatabaseHandler.KEY_STATUS, "UnDone");
		
		db.insert(DatabaseHandler.TABLE_TODO, null, val); 
	}
	
	public void deleteTodo(Todo todo){
		long id = todo.getId();
		db.delete(DatabaseHandler.TABLE_TODO, DatabaseHandler.KEY_ID + " = " + id, null);
	}
	
	public List<Todo> getAllTodos(){
		List<Todo> todos = new ArrayList<Todo>();
		String selectQuery = "SELECT * FROM " + DatabaseHandler.TABLE_TODO;
		Cursor c = db.rawQuery(selectQuery, null);
		
		if(c.moveToFirst()){
			do{
				Todo todo = new Todo();
				todo.setID(c.getInt(c.getColumnIndex(DatabaseHandler.KEY_ID)));
				todo.setNote(c.getString(c.getColumnIndex(DatabaseHandler.KEY_NOTE)));
				todo.setCategory(c.getString(c.getColumnIndex(DatabaseHandler.KEY_CATEGORY)));
				todo.setPrefLoc(c.getString(c.getColumnIndex(DatabaseHandler.KEY_SUBCATEGORYTODO)));
				todo.setStartDate(c.getString(c.getColumnIndex(DatabaseHandler.KEY_STARTDATE)));
				todo.setStatus(c.getString(c.getColumnIndex(DatabaseHandler.KEY_STATUS)));
				todos.add(todo);
			}while(c.moveToNext());
		}
		
		c.close();
		return todos;
	}
}
