package com.stalker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.stalker.DBHelper.Category;
import com.stalker.DBHelper.DatabaseHandler;
import com.stalker.DBHelper.TodoData;

import android.os.Bundle;
import android.app.Activity;
import android.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class AddToDoActivity extends FragmentActivity {

	Spinner catSpinner;
	Spinner prefLocSpinner;
	EditText notesTextField;
	
	private TodoData todoDat;
	//DatabaseHandler db = new DatabaseHandler(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_to_do);
		
		catSpinner = (Spinner) findViewById(R.id.categorySpinner);
		prefLocSpinner = (Spinner) findViewById(R.id.preferredLocationSpinner);
		notesTextField = (EditText) findViewById(R.id.notesTxtBox);
		
		loadCategoryAndPrefLocData();
		
		todoDat = new TodoData(this);
		todoDat.openDB();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_to_do, menu);
		return true;
	}
	
	public void loadCategoryAndPrefLocData(){
		DatabaseHandler db = new DatabaseHandler(this);
		
		List<Category> categories = db.getCategories();
		ArrayAdapter<Category> dataAdapter = new ArrayAdapter<Category>(this, 
				android.R.layout.simple_spinner_dropdown_item, categories);
		
		String [] stores = new String [] 
				{"Safeway","Walmart","Target","CVS","Walgreens","Lucky","Smart & Final",
				"Costco","99 Ranch","KOHL","Ross", "Lowes","Home Depot", "IKEA",
				"Bank of America","Chase","Well's Fargo","Citibank","US Bank",
				"Bank of West","Bank of Fremont","Starbucks","Jamba Juice",
				"Peet's Coffee & Tea", "Applebee's","iHop", "Denny's","McDonald's", 
				"Jack in the Box", "KFC", "Sushi", "Papa John's","Pizza Hut",
				"Domino's Pizza", "Shell", "Chevron", "Valero", "ARCO", "76"};
		ArrayAdapter<String> storeAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, stores);
		
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		catSpinner.setAdapter(dataAdapter);
		
		prefLocSpinner.setAdapter(storeAdapter);
		db.close();
	}
	
	
	//Save button ClickEventListener
	public void saveTodo(View v){
		String cat = catSpinner.getSelectedItem().toString();
		Log.d("ADDTODO","Selected category: " + cat);
		String prefPlace = prefLocSpinner.getSelectedItem().toString();
		Log.d("ADDTODO","Selected pref location: " + prefPlace);
		String notes = notesTextField.getText().toString();
		Log.d("ADDTODO","Notes: " + notes);
		
		//Calendar c = Calendar.getInstance();
		
		
		todoDat.createTodo(notes, cat, prefPlace, "April 25", "April 26", 0);
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
}
