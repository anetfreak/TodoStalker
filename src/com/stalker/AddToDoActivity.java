package com.stalker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.stalker.DBHelper.Category;
import com.stalker.DBHelper.DatabaseHandler;
import com.stalker.DBHelper.PreferredLocation;
import com.stalker.DBHelper.TodoData;

import android.os.Bundle;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class AddToDoActivity extends FragmentActivity implements OnItemSelectedListener{

	Spinner catSpinner;
	Spinner subCatSpinner;
	EditText notesTextField;
	public static int startYear;
	public static int startMonth;
	public static int startDay;
	
	private TodoData todoDat;
	//DatabaseHandler db = new DatabaseHandler(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_to_do);
		
		catSpinner = (Spinner) findViewById(R.id.categorySpinner);
		subCatSpinner = (Spinner) findViewById(R.id.subCategorySpinner);
		notesTextField = (EditText) findViewById(R.id.notesTxtBox);
		
		catSpinner.setOnItemSelectedListener(this);
		
		loadCategoryData();
		
		todoDat = new TodoData(this);
		todoDat.openDB();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_to_do, menu);
		return true;
	}
	
	public void loadCategoryData(){
		final DatabaseHandler db = new DatabaseHandler(this);
		
		List<Category> categories = db.getCategories();
		ArrayAdapter<Category> dataAdapter = new ArrayAdapter<Category>(this, 
				android.R.layout.simple_spinner_dropdown_item, categories);
		
		catSpinner.setAdapter(dataAdapter);
		
		db.close();
	}
	
	
	//Save button ClickEventListener
	public void saveTodo(View v){
		String cat = catSpinner.getSelectedItem().toString();
		Log.d("ADDTODO","Selected category: " + cat);
		String subCateg = subCatSpinner.getSelectedItem().toString();
		Log.d("ADDTODO","Selected sub category: " + subCateg);
		String notes = notesTextField.getText().toString();
		Log.d("ADDTODO","Notes: " + notes);
		
		StringBuilder s = new StringBuilder().append(startYear).append("/").
				append(startMonth+1).append("/").append(startDay);
		
		//Save todo in database
		todoDat.createTodo(notes, cat, subCateg, new String(s), 0);
		
		//Update the Map
		(new GetPlacesTask(this)).execute();
		
		//Go To ListToDo Activity
		Intent i = new Intent(getApplicationContext(), ListTodoActivity.class);
		startActivity(i);
	}
	
	//Cancel button ClickEventListener
	public void cancelOnClick(View v){
		Intent i = new Intent(getApplicationContext(), HomeScreenActivity.class);
		startActivity(i);
	}
	
	public void showDatePickerDialog(View v) {
	    DialogFragment newFragment = new DatePickerFragment();
	    newFragment.show(getFragmentManager(), "datePicker");
	}

	@Override
	protected void onPause() {
		todoDat.closeDB();
		super.onPause();
	}

	@Override
	protected void onResume() {
		todoDat.openDB();
		super.onResume();
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View arg1, int arg2, long itemPos){
		DatabaseHandler db = new DatabaseHandler(this);
		int pos = (int) itemPos + 1;
		
		List<String> pLocs = db.getSubCategory(pos);
		ArrayAdapter<String> locationAdapter = new 
					ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_dropdown_item, pLocs);
				
		subCatSpinner.setAdapter(locationAdapter);
		db.close();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}
}
