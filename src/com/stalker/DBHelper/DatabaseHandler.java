package com.stalker.DBHelper;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
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
	public static final String TABLE_CATEGORY = "Category";
	public static final String TABLE_PREFERREDLOCATION = "PreferredLocation";
	public static final String TABLE_TODO = "TodoList";
	public static final String KEY_ID = "id";
	public static final String ROW_ID = "rowid _id";
	
	//Category Table
	public static final String KEY_CATEGORYNAME = "category_name";
	private final String [] categories = new String[] 
			{"Shopping","Food & Drink","Travel","Home","Health & Medicine",
			"Bank/ATM","Fuel","Study","Entertainment","Other"};
	
	//ToDo List Table
	public static final String KEY_NOTE = "note";
	public static  String KEY_CATEGORY = "category";
	public static final String KEY_PREFLOC = "pref_location";
	public static final String KEY_STARTDATE = "start_date";
	public static final String KEY_PRIORITYID = "category_id";
	public static final String KEY_DUE = "due_date";
	public static final String KEY_STATUS = "status";
	
	//Preferred_Location Table
	public static final String KEY_CATEGORYID = "category_id";
	public static final String KEY_LOCNAME = "location_name";
	
	// Category table CREATE statements
	private static final String CREATE_TABLE_CATEGORY = "CREATE TABLE " + 
			TABLE_CATEGORY + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			KEY_CATEGORYNAME + " TEXT" + ")";
	
	public static final String[] ALL_KEYS = new String[] {ROW_ID, KEY_NOTE, KEY_CATEGORY, KEY_PREFLOC, KEY_STARTDATE, KEY_STATUS};

		
	//ToDo table CREATE statements
	private static final String CREATE_TABLE_TODO = "CREATE TABLE " + 
			TABLE_TODO + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			KEY_NOTE + " TEXT, " + KEY_CATEGORY + " TEXT, " + KEY_PREFLOC + 
			" TEXT, " + KEY_STARTDATE + " DATETIME, "+ KEY_STATUS + 
			" INTEGER NOT NULL" +")";
	
	//PreferredLocation table CREATE statements
	private static final String CREATE_TABLE_PREFLOC = "CREATE TABLE " +
			TABLE_PREFERREDLOCATION + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			KEY_CATEGORYID + " INTEGER, " + KEY_LOCNAME + " TEXT, " + "FOREIGN KEY(" +
			KEY_CATEGORYID + ") REFERENCES " + TABLE_CATEGORY + "(" + KEY_ID + ")" + ")";
	
	public DatabaseHandler(Context context) {
		super(context,DATABASE_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//Creating the DB Tables
		db.execSQL(CREATE_TABLE_CATEGORY);
		db.execSQL(CREATE_TABLE_PREFLOC);
		db.execSQL(CREATE_TABLE_TODO);
		
		//Populating Category Table
		for(String s:categories){
			String insertCateg = "INSERT INTO " + TABLE_CATEGORY + " (" + 
					KEY_CATEGORYNAME + ") " + "VALUES ('" + s + "')";
			db.execSQL(insertCateg);
		}
		
		/*String insertPrefLoc = "INSERT INTO " + TABLE_PREFERREDLOCATION + " (" + 
				KEY_CATEGORYID + "," + KEY_LOCNAME + ") " + "VALUES (" +
				1 + ", '" + "Safeway" + "')";
		db.execSQL(insertPrefLoc);*/
		
		createPrefLoc(db, 1, "Safeway");
		createPrefLoc(db, 1, "Walmart");
		createPrefLoc(db, 1, "Target");
		createPrefLoc(db, 2, "iHop");
		createPrefLoc(db, 2, "KFC");
		createPrefLoc(db, 3, "SFO Airport");
		createPrefLoc(db, 3, "VTA Station");
		createPrefLoc(db, 4, "Lowes");
		createPrefLoc(db, 4, "Home Depot");
		createPrefLoc(db, 4, "IKEA");
		createPrefLoc(db, 5, "Walgreens");
		createPrefLoc(db, 5, "CVS");
		createPrefLoc(db, 6, "Bank of America");
		createPrefLoc(db, 6, "Chase");
		createPrefLoc(db, 7, "Shell");
		createPrefLoc(db, 7, "Chevron");
		createPrefLoc(db, 8, "Martin Luther King Jr. Library");
		createPrefLoc(db, 9, "Great America");
		createPrefLoc(db, 9, "Regal Cinema");
		createPrefLoc(db, 10, "Laundromat");
		
	}
	
	// Open the database connection.
			public DatabaseHandler open() {
				db = getWritableDatabase();
				return this;
			}
			
			
			// Return all data in the database.
			public Cursor getAllRows() {
				String selectQuery = "SELECT * FROM " + TABLE_TODO+ " ORDER BY "+ KEY_STARTDATE;
									
				String where = null;
				Cursor c = 	db.query(true, TABLE_TODO, ALL_KEYS, 
									where, null, null, null,KEY_STARTDATE+" DESC ", null);
			
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
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
		db.execSQL("DROP TABLE IF EXISTS" + TABLE_PREFERREDLOCATION);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
		
		//create new table
		onCreate(db);
	}
	
	//CRUD(Create, Read, Update, Delete) Operations
	//-------------------------------PREFERRED_LOC TABLE-------------------------//
	public void createPrefLoc(SQLiteDatabase d, long location_id, String pLocation){
		String insertPrefLoc = "INSERT INTO " + TABLE_PREFERREDLOCATION + " (" + 
				KEY_CATEGORYID + "," + KEY_LOCNAME + ") " + "VALUES (" +
				location_id + ", '" + pLocation + "')";
		d.execSQL(insertPrefLoc);
	}
	
	public List<String> getPreferredLocations(int categID){
		List<String> pLocations = new ArrayList<String>();
		String selectQuery = "SELECT " + KEY_LOCNAME + " FROM " + TABLE_PREFERREDLOCATION +
				" WHERE " + KEY_CATEGORYID + " = " + categID;
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);
		if(c.moveToFirst()){
			do{
				String loc = c.getString(c.getColumnIndex(KEY_LOCNAME));
				
				pLocations.add(loc);
			}while(c.moveToNext());
		}
		
		c.close();
		db.close();
		
		return pLocations;
	}
	
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
					
				//Adding to categories list
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
					todo.setStatus(c.getInt(c.getColumnIndex(KEY_STATUS)));
						
					//Adding to ToDo list
					todoList.add(todo);
				} while(c.moveToNext());
			}
			
			Log.d("DBHANDLER", "Count: " + c.getCount());
			c.close();
			db.close();
			return todoList;
		}

		public Cursor getAllRowsCat(String category) {

			String selectQuery = "SELECT * FROM " + TABLE_TODO+ " ORDER BY "+ KEY_STARTDATE;
			
		//	String where = "KEY_CATEGORY = "+category;
			Cursor c = 	db.query(true, TABLE_TODO, ALL_KEYS, 
								KEY_CATEGORY + " = ?", new String[] {category}, null, null,KEY_STARTDATE+" DESC ", null);
		
			if (c != null) {
				Log.i("getrows", new Integer(c.getCount()).toString());
				c.moveToFirst();
			} else {
				Log.i("getrows", "cursor null");
			}
			
			return c;
			
		}
}
