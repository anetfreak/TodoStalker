package com.stalker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.stalker.DBHelper.DatabaseHandler;

import android.R;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;


public class ListTodoActivity extends Activity {
	DatabaseHandler myDB;
	int icon = R.drawable.map1;
	Cursor cursor;
	private final String [] categories = new String[] 
			{"Shopping","Food & Drink","Travel","Home","Health & Medicine",
			"Bank/ATM","Fuel","Study","Work","Other"};
	private final int[] colors = new int[] 
			{android.R.color.holo_red_light,android.R.color.holo_blue_light,android.R.color.darker_gray,android.R.color.holo_orange_light,
			android.R.color.holo_purple,android.R.color.holo_green_light,android.R.color.holo_blue_dark,android.R.color.holo_green_dark,
			android.R.color.holo_orange_dark,android.R.color.holo_red_dark};
	Map<String, String> catColor = new HashMap<String, String>();

	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_todo);
		openDB();
		//		myDB.deleteTableTododata();
		//		myDB.insertRow("01", "buy clothes","Shopping","2","date","Done");
		//		myDB.insertRow("02", "do grocery","Food & Drink", "2", "date", "Done");
		//		myDB.insertRow("03", "fill fuel","Fuel", "2", "date", "Done");
		//		myDB.insertRow("04", "call manager", "Work", "2", "date", "Done");
		//		myDB.insertRow("05", "book tickets", "Travel", "2", "date", "Done");
		//		myDB.insertRow("06", "furniture", "Home", "2", "date", "Done");
		//		myDB.insertRow("07", "withdraw money", "Bank/ATM", "2", "date", "Done");
		//		myDB.insertRow("08", "submit assignment", "Study", "2", "date", "Done");
		//		myDB.insertRow("09", "buy medicine", "Health & Medicine", "2", "date", "Done");
		//		myDB.insertRow("10", "call parents", "Other", "2", "date", "Done");
		//		myDB.insertRow("11", "gym", "Other", "2", "date", "Done");


		populateHashMap();

		populateTodoListFromDB();
	}

	private void populateHashMap() {
		int i=0;
		for (String cat:categories){
			catColor.put(cat,new Integer(colors[i]).toString());
			i++;

		}

	}

	protected void onDestroy(){
		super.onDestroy();
		closeDB();
	}

	private void openDB(){
		myDB = new DatabaseHandler(this);
		myDB.open();

	}


	private void closeDB(){
		myDB.close();
	}


	private void populateTodoListFromDB() {

		cursor = myDB.getAllRows();

		startManagingCursor(cursor);

		//Setup mapping from cursor to view fields
		String[] fieldNames = new String[]{
				DatabaseHandler.KEY_NOTE};

		int[] viewIds = new int[] {R.id.item_todo};


		//Create Adapter to map columns of DB to the view
		SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, R.layout.icon_view, cursor, fieldNames, viewIds){

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {


				final View row = super.getView(position, convertView, parent);


				cursor.moveToPosition(position);
				String category = cursor.getString(2);
				Log.i("ct", category);

				int color = Integer.parseInt(catColor.get(category));
				row.setBackgroundColor(getResources().getColor(color));

				return row;
			}
		};

		ListView list = (ListView) findViewById(R.id.listView1);
		list.setAdapter(cursorAdapter);
	}	

}
