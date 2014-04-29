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
	
	//Tables name
	//private static final String TABLE_PRIORITY = "Priority";
	public static final String TABLE_CATEGORY = "Category";
	//private static final String TABLE_STORES = "Stores";
	//private static final String TABLE_PREFERREDLOCATION = "PreferredLocation";
	public static final String TABLE_TODO = "TodoList";
	
	public static final String KEY_ID = "id";
	
	//Priority Table
	/*private static final String KEY_PRIORITYNAME = "priority_name";
	private static final String KEY_PRIORITYNUM = "priority_num";*/
	
	//Category Table
	public static final String KEY_CATEGORYNAME = "category_name";
	private final String [] categories = new String[] 
			{"Shopping","Food & Drink","Travel","Home","Health & Medicine",
			"Bank/ATM","Fuel","Study","Work","Other"};
	
	//Stores Table
	/*private static final String KEY_STORENAME = "store_name";
	private final String [] stores = new String [] 
			{"Safeway","Walmart","Target","CVS","Walgreens","Lucky","Smart & Final",
			"Costco","99 Ranch","KOHL","Ross", "Lowes","Home Depot", "IKEA",
			"Bank of America","Chase","Well''s Fargo","Citibank","US Bank",
			"Bank of West","Bank of Fremont","Starbucks","Jamba Juice",
			"Peet''s Coffee & Tea", "Applebee''s","iHop", "Denny''s","McDonald''s", 
			"Jack in the Box", "KFC", "Sushi", "Papa John''s","Pizza Hut",
			"Domino''s Pizza", "Shell", "Chevron", "Valero", "ARCO", "76"};*/
	
	//Preferred Location Table
	//private static final String KEY_STORESID = "store_id";
	
	//ToDo List Table
	public static final String KEY_NOTE = "note";
	public static final String KEY_CATEGORY = "category";
	public static final String KEY_PREFLOC = "pref_location";
	//private static final String KEY_PRIORITYID = "category_id";
	public static final String KEY_STARTDATE = "start_date";
	public static final String KEY_ENDDATE = "end_date";
	public static final String KEY_STATUS = "status";
	
	/*//Priority table CREATE statements
	private static final String CREATE_TABLE_PRIORITY = "CREATE TABLE " + 
			TABLE_PRIORITY + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			KEY_PRIORITYNAME + " TEXT, " + KEY_PRIORITYNUM + " INTEGER" + ")";*/
	
	// Category table CREATE statements
	private static final String CREATE_TABLE_CATEGORY = "CREATE TABLE " + 
			TABLE_CATEGORY + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			KEY_CATEGORYNAME + " TEXT" + ")";
	
	// Stores table CREATE statements
	/*private static final String CREATE_TABLE_STORES = "CREATE TABLE " + 
			TABLE_STORES + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			KEY_STORENAME + " TEXT" + ")";*/
	
	// Preferred Location table CREATE statements
	/*private static final String CREATE_TABLE_PREFLOC = "CREATE TABLE " +
			TABLE_PREFERREDLOCATION + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			KEY_CATEGORYID + " INTEGER, " + KEY_STORESID + " INTEGER" + ")";*/
		
	//ToDo table CREATE statements
	private static final String CREATE_TABLE_TODO = "CREATE TABLE " + 
			TABLE_TODO + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			KEY_NOTE + " TEXT, " + KEY_CATEGORY + " TEXT, " + KEY_PREFLOC + 
			" TEXT, " + KEY_STARTDATE + " DATETIME, " + KEY_ENDDATE + " DATETIME, " + 
			KEY_STATUS + " INTEGER NOT NULL" +")";
	
	/*//Priority table INSERT statements
	private static final String INSERT_PRIORITY_LOW = "INSERT INTO " + 
			TABLE_PRIORITY + " (" + KEY_PRIORITYNAME + ", " + KEY_PRIORITYNUM + ") " +
			"VALUES (Low, 1)";
	
	private static final String INSERT_PRIORITY_MEDIUM = "INSERT INTO " + 
			TABLE_PRIORITY + " (" + KEY_PRIORITYNAME + ", " + KEY_PRIORITYNUM + ") " +
			"VALUES (Medium, 2)";
	
	private static final String INSERT_PRIORITY_HIGH = "INSERT INTO " + 
			TABLE_PRIORITY + " (" + KEY_PRIORITYNAME + ", " + KEY_PRIORITYNUM + ") " +
			"VALUES (High, 3)";*/
	
	
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
		
		/*//Populating Priority Table
		db.execSQL(INSERT_PRIORITY_LOW);
		db.execSQL(INSERT_PRIORITY_MEDIUM);
		db.execSQL(INSERT_PRIORITY_HIGH);*/
		
		//Populating Category Table
		for(String s:categories){
			String insertCateg = "INSERT INTO " + TABLE_CATEGORY + " (" + 
					KEY_CATEGORYNAME + ") " + "VALUES ('" + s + "')";
			db.execSQL(insertCateg);
		}
		
		//Populating Stores Table. MIGHT HAVE TO POPULATE THIS FIRST THEN POPULATE CATEG
		/*for(String s:stores){
			String insertStore = "INSERT INTO " + TABLE_STORES + " (" + 
					KEY_STORENAME + ") " + "VALUES ('" + s + "')";
			db.execSQL(insertStore);
		}*/
	
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
	/*//-----------------------------------PRIORITY TABLE-------------------------//
	
	//getting all priorities
	public List<Priority> getPriorities(){
		List<Priority> priorities = new ArrayList<Priority>();
		String selectQuery = "SELECT * FROM " + TABLE_PRIORITY;
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);
		
		//Looping through the rows
		if(c.moveToFirst()){
			do{
				Priority p = new Priority();
				p.setID(c.getInt(c.getColumnIndex(KEY_ID)));
				p.setPriorityName(c.getString(c.getColumnIndex(KEY_PRIORITYNAME)));
				p.setPriorityNum(c.getInt(c.getColumnIndex(KEY_PRIORITYNUM)));
				
				//Adding to priority list
				priorities.add(p);
			} while(c.moveToNext());
		}
		c.close();
		db.close();
		return priorities;
	}*/
	
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
	
}
