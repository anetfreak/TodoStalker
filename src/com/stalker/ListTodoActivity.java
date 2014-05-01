package com.stalker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.stalker.DBHelper.DatabaseHandler;
import com.stalker.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;


public class ListTodoActivity extends Activity implements OnItemSelectedListener{
	DatabaseHandler myDB;
	int icon = R.drawable.map1;
	String categoryName;
	Cursor cursor;
	Spinner spinner;
	private final String [] categories = new String[] 
			{"Shopping","Food & Drink","Travel","Home","Health & Medicine",
			"Bank/ATM","Fuel","Study","Work","Other"};
	private final int[] colors = new int[] 
			{android.R.color.holo_red_light,android.R.color.holo_blue_light,android.R.color.darker_gray,android.R.color.holo_orange_light,
			android.R.color.holo_purple,android.R.color.holo_green_light,android.R.color.holo_blue_dark,android.R.color.holo_green_dark,
			android.R.color.holo_orange_dark,android.R.color.holo_red_dark};
	Map<String, String> catColor = new HashMap<String, String>();
	
	private final String [] categoryArray = new String[] 
			{"All","Shopping","Food & Drink","Travel","Home","Health & Medicine",
			"Bank/ATM","Fuel","Study","Work","Other"};
	

	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_todo);
		
		spinner = (Spinner)findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ListTodoActivity.this,
                android.R.layout.simple_spinner_item,categoryArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        categoryName = "All";
		openDB();
		
		populateHashMap();

		populateTodoListFromDB(categoryName);
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


	private void populateTodoListFromDB(String category) {
		
		if(category.equalsIgnoreCase("All")){
			
			cursor = myDB.getAllRows();
		}else{
			cursor = myDB.getAllRowsCat(category);
		}

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
	
	public void viewMap(View view){
		// display todo location on map
		Intent i = new Intent(getApplicationContext(),MapAllTODOs.class);
		i.putExtra("identifier", 2);
		startActivity(i);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		spinner.setSelection(position);
		  String selState = (String) spinner.getSelectedItem();
		  populateTodoListFromDB(selState);
		  
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

}
