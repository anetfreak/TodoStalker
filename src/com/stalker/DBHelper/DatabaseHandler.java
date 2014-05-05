package com.stalker.DBHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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

	public static String Id = "_id";
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
		
		createPrefLoc(db, 1, "clothing_store");
		createPrefLoc(db, 1, "electronics_store");
		createPrefLoc(db, 1, "department_store");
		createPrefLoc(db, 1, "grocery_or_supermarket");
		createPrefLoc(db, 1, "liquor_store");
		createPrefLoc(db, 1, "jewelry_store");
		createPrefLoc(db, 1, "shoe_store");
		createPrefLoc(db, 1, "shopping_mall");
		createPrefLoc(db, 1, "pet_store");
		createPrefLoc(db, 1, "bicycle_store");
		createPrefLoc(db, 2, "bakery");
		createPrefLoc(db, 2, "bar");
		createPrefLoc(db, 2, "cafe");
		createPrefLoc(db, 2, "casino");
		createPrefLoc(db, 2, "food");
		createPrefLoc(db, 2, "meal_delivery");
		createPrefLoc(db, 2, "meal_takeaway");
		createPrefLoc(db, 2, "restaurant");
		createPrefLoc(db, 3, "airport");
		createPrefLoc(db, 3, "lodging");
		createPrefLoc(db, 3, "taxi_stand");
		createPrefLoc(db, 3, "train_station");
		createPrefLoc(db, 3, "travel_agency");
		createPrefLoc(db, 3, "car_rental");
		createPrefLoc(db, 3, "subway_station");
		createPrefLoc(db, 4, "home_goods_store");
		createPrefLoc(db, 4, "furniture_store");
		createPrefLoc(db, 4, "grocery_or_supermarket");
		createPrefLoc(db, 4, "hardware_store");
		createPrefLoc(db, 5, "dentist");
		createPrefLoc(db, 5, "doctor");
		createPrefLoc(db, 5, "gym");
		createPrefLoc(db, 5, "hospital");
		createPrefLoc(db, 5, "health");
		createPrefLoc(db, 5, "pharmacy");
		createPrefLoc(db, 5, "physiotherapist");
		createPrefLoc(db, 5, "spa");
		createPrefLoc(db, 5, "veterinary_care");
		createPrefLoc(db, 6, "atm");
		createPrefLoc(db, 6, "bank");
		createPrefLoc(db, 6, "finance");
		createPrefLoc(db, 7, "gas_station");
		createPrefLoc(db, 8, "library");
		createPrefLoc(db, 8, "school");
		createPrefLoc(db, 8, "university");
		createPrefLoc(db, 8, "book_store");
		createPrefLoc(db, 9, "amusement_park");
		createPrefLoc(db, 9, "aquarium");
		createPrefLoc(db, 9, "art_gallery");
		createPrefLoc(db, 9, "bowling_alley");
		createPrefLoc(db, 9, "casino");
		createPrefLoc(db, 9, "movie_rental");
		createPrefLoc(db, 9, "movie_theater");
		createPrefLoc(db, 9, "museum");
		createPrefLoc(db, 9, "night_club");
		createPrefLoc(db, 9, "park");
		createPrefLoc(db, 9, "stadium");
		createPrefLoc(db, 9, "zoo");
		createPrefLoc(db, 10, "beauty_salon");
		createPrefLoc(db, 10, "car_dealer");
		createPrefLoc(db, 10, "car_rental");
		createPrefLoc(db, 10, "car_repair");
		createPrefLoc(db, 10, "car_wash");
		createPrefLoc(db, 10, "florist");
		createPrefLoc(db, 10, "laundry");
		createPrefLoc(db, 10, "place_of_worship");
		createPrefLoc(db, 10, "storage");
		
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
									where, null, null, null,KEY_STARTDATE+" ASC ", null);
			
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
					todo.setID(c.getInt(c.getColumnIndex(KEY_ID)));
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
		//	db.close();
			return todoList;
		}

		public Cursor getAllRowsCat(String category) {

			String selectQuery = "SELECT * FROM " + TABLE_TODO+ " ORDER BY "+ KEY_STARTDATE;
			
		//	String where = "KEY_CATEGORY = "+category;
			Cursor c = 	db.query(true, TABLE_TODO, ALL_KEYS, 
								KEY_CATEGORY + " = ?", new String[] {category}, null, null,KEY_STARTDATE+" ASC ", null);
		
			if (c != null) {
				Log.i("getrows", new Integer(c.getCount()).toString());
				c.moveToFirst();
			} else {
				Log.i("getrows", "cursor null");
			}
			
			return c;
			
		}


		public Cursor getAllRowsDat(String date) {
			String selectQuery = "SELECT * FROM " + TABLE_TODO+ " ORDER BY "+ KEY_STARTDATE;
			
			//	String where = "KEY_CATEGORY = "+category;
				Cursor c = 	db.query(true, TABLE_TODO, ALL_KEYS, 
									KEY_STARTDATE + " = ?", new String[] {date}, null, null,KEY_STARTDATE+" ASC ", null);
			
				if (c != null) {
					Log.i("getrows", new Integer(c.getCount()).toString());
					c.moveToFirst();
				} else {
					Log.i("getrows", "cursor null");
				}
				
				return c;
		}

		public Cursor getAllRowsCatDate(String category, String date) {
			
				String selectQuery = "SELECT * FROM " + TABLE_TODO+ " ORDER BY "+ KEY_STARTDATE;
				
				//	String where = "KEY_CATEGORY = "+category;
					Cursor c = 	db.query(true, TABLE_TODO, ALL_KEYS, KEY_CATEGORY + " = ? " + " and "+
										KEY_STARTDATE + " = ?", new String[] {category,date}, null, null,KEY_STARTDATE+" ASC ", null);
				
					if (c != null) {
						Log.i("getrows", new Integer(c.getCount()).toString());
						c.moveToFirst();
					} else {
						Log.i("getrows", "cursor null");
					}
					
					return c;
		}
}
