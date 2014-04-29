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
	private static final String TABLE_PRIORITY = "priority";
	private static final String TABLE_CATEGORY = "category";
	private static final String TABLE_TODO = "todoList";
	
	private static final String KEY_ID = "id";
	public static final String ROW_ID = "rowid _id";
	//Priority Table
	private static final String KEY_PRIORITYNAME = "priority_name";
	private static final String KEY_PRIORITYNUM = "priority_num";
	
	//Category Table
	private static final String KEY_CATEGORYNAME = "category_name";
	private final String [] categories = new String[] 
			{"Shopping","Food & Drink","Travel","Home","Health & Medicine",
			"Bank/ATM","Fuel","Study","Work","Other"};
	
	//ToDo List Table
	public static final String KEY_NOTE = "note";
	public static final String KEY_CATEGORYID = "category_id";
	public static final String KEY_PRIORITYID = "category_id";
	public static final String KEY_DUE = "due_date";
	public static final String KEY_STATUS = "status";
	
	/*//Priority table CREATE statements
	private static final String CREATE_TABLE_PRIORITY = "CREATE TABLE " + 
			TABLE_PRIORITY + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			KEY_PRIORITYNAME + " TEXT, " + KEY_PRIORITYNUM + " INTEGER" + ")";*/
	
	// Category table CREATE statements
	private static final String CREATE_TABLE_CATEGORY = "CREATE TABLE " + 
			TABLE_CATEGORY + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			KEY_CATEGORYNAME + " TEXT" + ")";
	
	public static final String[] ALL_KEYS = new String[] {ROW_ID, KEY_NOTE, KEY_CATEGORYID, KEY_PRIORITYID, KEY_DUE, KEY_STATUS};

		
	/*//ToDo table CREATE statements
	private static final String CREATE_TABLE_TODO = "CREATE TABLE " + 
			TABLE_TODO + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			KEY_NOTE + " TEXT, " + KEY_CATEGORYID + " INTEGER, " + KEY_PRIORITYID + 
			" INTEGER, " + KEY_DUE + " DATETIME, " + KEY_STATUS + " INTEGER" +")";*/
	
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
	
	/*//Category table INSERT statements
	private static final String INSERT_CATEGORY_1 = "INSERT INTO " + 
			TABLE_CATEGORY + " (" + KEY_CATEGORYNAME + ") " + "VALUES ('Shopping')";
		
	private static final String INSERT_CATEGORY_2 = "INSERT INTO " + 
			TABLE_CATEGORY + " (" + KEY_CATEGORYNAME + ") " + "VALUES ('Food & Drink')";
		
	private static final String INSERT_CATEGORY_3 = "INSERT INTO " + 
			TABLE_CATEGORY + " (" + KEY_CATEGORYNAME + ") " + "VALUES ('Travel')";
	
	private static final String INSERT_CATEGORY_4 = "INSERT INTO " + 
			TABLE_CATEGORY + " (" + KEY_CATEGORYNAME + ") " + "VALUES ('Home')";
		
	private static final String INSERT_CATEGORY_5 = "INSERT INTO " + 
			TABLE_CATEGORY + " (" + KEY_CATEGORYNAME + ") " + "VALUES ('Health & Medicine')";
		
	private static final String INSERT_CATEGORY_6 = "INSERT INTO " + 
			TABLE_CATEGORY + " (" + KEY_CATEGORYNAME + ") " + "VALUES ('Bank/ATM')";
	
	private static final String INSERT_CATEGORY_7 = "INSERT INTO " + 
			TABLE_CATEGORY + " (" + KEY_CATEGORYNAME + ") " + "VALUES ('Fuel')";
		
	private static final String INSERT_CATEGORY_8 = "INSERT INTO " + 
			TABLE_CATEGORY + " (" + KEY_CATEGORYNAME + ") " + "VALUES ('Study')";
		
	private static final String INSERT_CATEGORY_9 = "INSERT INTO " + 
			TABLE_CATEGORY + " (" + KEY_CATEGORYNAME + ") " + "VALUES ('Work')";
	
	private static final String INSERT_CATEGORY_10 = "INSERT INTO " + 
			TABLE_CATEGORY + " (" + KEY_CATEGORYNAME + ") " + "VALUES ('Other')";*/
	
	public DatabaseHandler(Context context) {
		super(context,DATABASE_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//Creating the DB Tables
		//db.execSQL(CREATE_TABLE_PRIORITY);
		db.execSQL(CREATE_TABLE_CATEGORY);
		//db.execSQL(CREATE_TABLE_TODO);
		
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
		/*db.execSQL(INSERT_CATEGORY_1);
		db.execSQL(INSERT_CATEGORY_2);
		db.execSQL(INSERT_CATEGORY_3);
		db.execSQL(INSERT_CATEGORY_4);
		db.execSQL(INSERT_CATEGORY_5);
		db.execSQL(INSERT_CATEGORY_6);
		db.execSQL(INSERT_CATEGORY_7);
		db.execSQL(INSERT_CATEGORY_8);
		db.execSQL(INSERT_CATEGORY_9);
		db.execSQL(INSERT_CATEGORY_10);*/
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
		//db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
		
		//create new table
		onCreate(db);
	}
	
	//CRUD(Create, Read, Update, Delete) Operations
	//-----------------------------------PRIORITY TABLE-------------------------//
	
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
}
