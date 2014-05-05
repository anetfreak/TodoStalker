package com.stalker;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.stalker.DBHelper.DatabaseHandler;


public class ListTodoActivity extends Activity implements OnItemSelectedListener{
	DatabaseHandler myDB;
	int icon = R.drawable.map1;
	String categoryName;
	Cursor cursor;
	Spinner spinner1;
	Spinner spinner2;
	private Button btnAdd;

	private final String [] categories = new String[] 
			{"Shopping","Food & Drink","Travel","Home","Health & Medicine",
			"Bank/ATM","Fuel","Study","Entertainment","Other"};

	private final String[] colors = new String[] 
			{"#78B7F6","#5E5EE7","#65EDED","#66ED66","#ED65ED","#EDA865","#E55B5B","#ED65A8","#A35DE8","#EDED65"};
	Map<String, String> catColor = new HashMap<String, String>();

	private final String [] categoryArray = new String[] 
			{"All","Shopping","Food & Drink","Travel","Home","Health & Medicine",
			"Bank/ATM","Fuel","Study","Work","Other"};


	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_todo);

		spinner1 = (Spinner)findViewById(R.id.spinner1);
		//spinner2 = 

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(ListTodoActivity.this,
				android.R.layout.simple_spinner_item,categoryArray);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner1.setAdapter(adapter);
		spinner1.setOnItemSelectedListener(this);
		categoryName = "All";
		openDB();

		populateHashMap();

		populateTodoListFromDB(categoryName);

		final Intent addIntent = new Intent(getApplicationContext(), AddToDoActivity.class);

		btnAdd = (Button) findViewById(R.id.button1);

		btnAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(addIntent);
			}
		});
	}

	private void populateHashMap() {
		int i=0;
		for (String cat:categories){
			catColor.put(cat,(colors[i]));
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

				row.setBackgroundColor(Color.parseColor(catColor.get(category)));

				return row;
			}
		};

		ListView list = (ListView) findViewById(R.id.notificationsList);
		list.setAdapter(cursorAdapter);
	}	

	public void viewMap(View view){
		// display todo location on map
		Intent i = new Intent(getApplicationContext(),MapAllTODOs.class);
		i.putExtra("identifier", 1);
		startActivity(i);
	}


	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		spinner1.setSelection(position);
		String selState = (String) spinner1.getSelectedItem();
		populateTodoListFromDB(selState);

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

}
