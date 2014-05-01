package com.stalker.DBHelper;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper{

	//Logcat tag
	private static final String LOG = "DatabaseHandler";
	
	//DB Name
	private static final String DATABASE_NAME = "todosDB.db";
	
	private SQLiteDatabase db;
	//Tables name
	//private static final String TABLE_PRIORITY = "Priority";
	public static final String TABLE_CATEGORY = "Category";
	//private static final String TABLE_STORES = "Stores";
	//private static final String TABLE_PREFERREDLOCATION = "PreferredLocation";
	public static final String TABLE_TODO = "TodoList";
	public static final String KEY_ID = "id";
	public static final String ROW_ID = "rowid _id";
	
	//Category Table
	public static final String KEY_CATEGORYNAME = "category_name";
	private final String [] categories = new String[] 
			{"Shopping","Food & Drink","Travel","Home","Health & Medicine",
			"Bank/ATM","Fuel","Study","Work","Other"};
	
	//ToDo List Table
	public static final String KEY_NOTE = "note";
	public static final String KEY_CATEGORY = "category";
	public static final String KEY_PREFLOC = "pref_location";
	//private static final String KEY_PRIORITYID = "category_id";
	public static final String KEY_STARTDATE = "start_date";
	//public static final String KEY_ENDDATE = "end_date";
	public static final String KEY_CATEGORYID = "category_id";
	public static final String KEY_PRIORITYID = "category_id";
	public static final String KEY_DUE = "due_date";
	public static final String KEY_STATUS = "status";
	
	// Category table CREATE statements
	private static final String CREATE_TABLE_CATEGORY = "CREATE TABLE " + 
			TABLE_CATEGORY + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			KEY_CATEGORYNAME + " TEXT" + ")";
	
	public static final String[] ALL_KEYS = new String[] {ROW_ID, KEY_NOTE, KEY_CATEGORYID, KEY_PRIORITYID, KEY_DUE, KEY_STATUS};

		
	//ToDo table CREATE statements
	private static final String CREATE_TABLE_TODO = "CREATE TABLE " + 
			TABLE_TODO + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			KEY_NOTE + " TEXT, " + KEY_CATEGORY + " TEXT, " + KEY_PREFLOC + 
			" TEXT, " + KEY_STARTDATE + " DATETIME, "+ KEY_STATUS + 
			" INTEGER NOT NULL" +")";
	
	public DatabaseHandler(Context context) {
		super(context,DATABASE_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//Creating the DB Tables
		//db.execSQL(CREATE_TABLE_PRIORITY);
		db.execSQL(CREATE_TABLE_CATEGORY);
		//db.execSQL(CREATE_TABLE_STORES);
		//db.execSQL(CREATE_TABLE_PREFLOC);
		db.execSQL(CREATE_TABLE_TODO);
		
		//Populating Category Table
		for(String s:categories){
			String insertCateg = "INSERT INTO " + TABLE_CATEGORY + " (" + 
					KEY_CATEGORYNAME + ") " + "VALUES ('" + s + "')";
			db.execSQL(insertCateg);
		}
		
	}
	
	// Open the database connection.
			public DatabaseHandler open() {
				db = getWritableDatabase();
				return this;
			}
			
			
			// Return all data in the database.
			public Cursor getAllRows() {
				String where = null;
				Cursor c = 	db.query(true, TABLE_TODO, ALL_KEYS, 
									where, null, null, null, null, null);
			
				if (c != null) {
					Log.i("getrows", new Integer(c.getCount()).toString());
					c.moveToFirst();
				} else {
					Log.i("getrows", "cursor null");
				}
				
				return c;
			}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//drop older tables
		//db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRIORITY);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
		//db.execSQL("DROP TABLE IF EXISTS" + TABLE_STORES);
		//db.execSQL("DROP TABLE IF EXISTS" + TABLE_PREFERREDLOCATION);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
		
		//create new table
		onCreate(db);
	}
	
	//CRUD(Create, Read, Update, Delete) Operations
	
	//-----------------------------------CATEGORY TABLE-------------------------//
	//getting all categories
	public List<Category> getCategories(){
		List<Category> categories = new ArrayList<Category>();
		String selectQuery = "SELECT * FROM " + TABLE_CATEGORY;
			
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);
			
		//Looping through the rows
		if(c.moveToFirst()){
			do{
				Category cat = new Category();
				cat.setID(c.getInt(c.getColumnIndex(KEY_ID)));
				cat.setCategoryName(c.getString(c.getColumnIndex(KEY_CATEGORYNAME)));
					
				//Adding to priority list
				categories.add(cat);
			} while(c.moveToNext());
		}
		
		Log.d("DBHANDLER", "Count: " + c.getCount());
		c.close();
		db.close();
		return categories;
	}
	
	//closing database
	public void closeDB(){
		SQLiteDatabase db = this.getReadableDatabase();
		if(db !=null && db.isOpen()){
			db.close();
		}
	}
	
	//-----------------------------------TODO TABLE-------------------------//
	
	
	//getting all todos
		public List<Todo> getAllTodos(){
			List<Todo> todoList = new ArrayList<Todo>();
			String selectQuery = "SELECT * FROM " + TABLE_TODO;
				
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor c = db.rawQuery(selectQuery, null);
				
			//Looping through the rows
			
			if(c.moveToFirst()){
				do{
					Todo todo = new Todo();
					todo.setNote(c.getString(c.getColumnIndex(KEY_NOTE)));
					todo.setCategory(c.getString(c.getColumnIndex(KEY_CATEGORY)));
					todo.setPrefLoc(c.getString(c.getColumnIndex(KEY_PREFLOC)));
					todo.setStartDate(c.getString(c.getColumnIndex(KEY_STARTDATE)));
					//todo.setEndDate(c.getString(c.getColumnIndex(KEY_ENDDATE)));
					todo.setStatus(c.getInt(c.getColumnIndex(KEY_STATUS)));
						
					//Adding to priority list
					todoList.add(todo);
				} while(c.moveToNext());
			}
			
			Log.d("DBHANDLER", "Count: " + c.getCount());
			c.close();
			db.close();
			return todoList;
		}
}
