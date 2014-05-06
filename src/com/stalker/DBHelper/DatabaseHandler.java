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
	public static final String TABLE_SUBCATEGORY = "SubCategory";
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
	public static final String KEY_SUBCATEGORYTODO = "sub_category";
	public static final String KEY_STARTDATE = "start_date";
	public static final String KEY_PRIORITYID = "category_id";
	public static final String KEY_DUE = "due_date";
	public static final String KEY_STATUS = "status";
	
	//Sub_Category Table
	public static final String KEY_CATEGORYID = "category_id";
	public static final String KEY_SUBCATEGORYNAME = "sub_category_name";
	
	// Category table CREATE statements
	private static final String CREATE_TABLE_CATEGORY = "CREATE TABLE " + 
			TABLE_CATEGORY + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			KEY_CATEGORYNAME + " TEXT" + ")";
	
	public static final String[] ALL_KEYS = new String[] {ROW_ID, KEY_NOTE, KEY_CATEGORY, 
					KEY_SUBCATEGORYTODO, KEY_STARTDATE, KEY_STATUS};

		
	//ToDo table CREATE statements
	private static final String CREATE_TABLE_TODO = "CREATE TABLE " + 
			TABLE_TODO + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			KEY_NOTE + " TEXT, " + KEY_CATEGORY + " TEXT, " + KEY_SUBCATEGORYTODO + 
			" TEXT, " + KEY_STARTDATE + " DATETIME, "+ KEY_STATUS + 
			" INTEGER NOT NULL" +")";
	
	//SubCategory table CREATE statements
	private static final String CREATE_TABLE_SUBCATEGORY = "CREATE TABLE " +
			TABLE_SUBCATEGORY + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			KEY_CATEGORYID + " INTEGER, " + KEY_SUBCATEGORYNAME + " TEXT, " + "FOREIGN KEY(" +
			KEY_CATEGORYID + ") REFERENCES " + TABLE_CATEGORY + "(" + KEY_ID + ")" + ")";
	
	public DatabaseHandler(Context context) {
		super(context,DATABASE_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//Creating the DB Tables
		db.execSQL(CREATE_TABLE_CATEGORY);
		db.execSQL(CREATE_TABLE_SUBCATEGORY);
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
		
		createSubCategory(db, 1, "clothing_store");
		createSubCategory(db, 1, "electronics_store");
		createSubCategory(db, 1, "department_store");
		createSubCategory(db, 1, "grocery_or_supermarket");
		createSubCategory(db, 1, "liquor_store");
		createSubCategory(db, 1, "jewelry_store");
		createSubCategory(db, 1, "shoe_store");
		createSubCategory(db, 1, "shopping_mall");
		createSubCategory(db, 1, "pet_store");
		createSubCategory(db, 1, "bicycle_store");
		createSubCategory(db, 2, "bakery");
		createSubCategory(db, 2, "bar");
		createSubCategory(db, 2, "cafe");
		createSubCategory(db, 2, "casino");
		createSubCategory(db, 2, "food");
		createSubCategory(db, 2, "meal_delivery");
		createSubCategory(db, 2, "meal_takeaway");
		createSubCategory(db, 2, "restaurant");
		createSubCategory(db, 3, "airport");
		createSubCategory(db, 3, "lodging");
		createSubCategory(db, 3, "taxi_stand");
		createSubCategory(db, 3, "train_station");
		createSubCategory(db, 3, "travel_agency");
		createSubCategory(db, 3, "car_rental");
		createSubCategory(db, 3, "subway_station");
		createSubCategory(db, 4, "home_goods_store");
		createSubCategory(db, 4, "furniture_store");
		createSubCategory(db, 4, "grocery_or_supermarket");
		createSubCategory(db, 4, "hardware_store");
		createSubCategory(db, 5, "dentist");
		createSubCategory(db, 5, "doctor");
		createSubCategory(db, 5, "gym");
		createSubCategory(db, 5, "hospital");
		createSubCategory(db, 5, "health");
		createSubCategory(db, 5, "pharmacy");
		createSubCategory(db, 5, "physiotherapist");
		createSubCategory(db, 5, "spa");
		createSubCategory(db, 5, "veterinary_care");
		createSubCategory(db, 6, "atm");
		createSubCategory(db, 6, "bank");
		createSubCategory(db, 6, "finance");
		createSubCategory(db, 7, "gas_station");
		createSubCategory(db, 8, "library");
		createSubCategory(db, 8, "school");
		createSubCategory(db, 8, "university");
		createSubCategory(db, 8, "book_store");
		createSubCategory(db, 9, "amusement_park");
		createSubCategory(db, 9, "aquarium");
		createSubCategory(db, 9, "art_gallery");
		createSubCategory(db, 9, "bowling_alley");
		createSubCategory(db, 9, "casino");
		createSubCategory(db, 9, "movie_rental");
		createSubCategory(db, 9, "movie_theater");
		createSubCategory(db, 9, "museum");
		createSubCategory(db, 9, "night_club");
		createSubCategory(db, 9, "park");
		createSubCategory(db, 9, "stadium");
		createSubCategory(db, 9, "zoo");
		createSubCategory(db, 10, "beauty_salon");
		createSubCategory(db, 10, "car_dealer");
		createSubCategory(db, 10, "car_rental");
		createSubCategory(db, 10, "car_repair");
		createSubCategory(db, 10, "car_wash");
		createSubCategory(db, 10, "florist");
		createSubCategory(db, 10, "laundry");
		createSubCategory(db, 10, "place_of_worship");
		createSubCategory(db, 10, "storage");
		
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
		db.execSQL("DROP TABLE IF EXISTS" + TABLE_SUBCATEGORY);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
		
		//create new table
		onCreate(db);
	}
	
	//CRUD(Create, Read, Update, Delete) Operations
	//-------------------------------SUB_CATEGORY TABLE-------------------------//
	public void createSubCategory(SQLiteDatabase d, long location_id, String subCategName){
		String insertSubCategory = "INSERT INTO " + TABLE_SUBCATEGORY + " (" + 
				KEY_CATEGORYID + "," + KEY_SUBCATEGORYNAME + ") " + "VALUES (" +
				location_id + ", '" + subCategName + "')";
		d.execSQL(insertSubCategory);
	}
	
	public List<String> getSubCategory(int categID){
		List<String> subCategories = new ArrayList<String>();
		subCategories.add("All");
		String selectQuery = "SELECT " + KEY_SUBCATEGORYNAME + " FROM " 
				+ TABLE_SUBCATEGORY + " WHERE " + KEY_CATEGORYID + " = " + categID;
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);
		if(c.moveToFirst()){
			do{
				String loc = c.getString(c.getColumnIndex(KEY_SUBCATEGORYNAME));
				
				subCategories.add(loc);
			}while(c.moveToNext());
		}
		
		//Closing DB
		c.close();
		db.close();
		
		return subCategories;
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
		
		//Closing DB
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
					todo.setPrefLoc(c.getString(c.getColumnIndex(KEY_SUBCATEGORYTODO)));
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
