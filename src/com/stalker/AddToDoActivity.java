package com.stalker;

import java.util.List;

import com.stalker.DBHelper.Category;
import com.stalker.DBHelper.DatabaseHandler;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class AddToDoActivity extends Activity {

	Spinner catSpinner;
	Spinner prefLocSpinner;
	DatabaseHandler db = new DatabaseHandler(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_to_do);
		
		catSpinner = (Spinner) findViewById(R.id.categorySpinner);
		prefLocSpinner = (Spinner) findViewById(R.id.preferredLocationSpinner);
		
		
		loadCategoryData();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_to_do, menu);
		return true;
	}
	
	public void loadCategoryData(){
		//DatabaseHandler db = new DatabaseHandler(this);
		
		List<Category> categories = db.getCategories();
		ArrayAdapter<Category> dataAdapter = new ArrayAdapter<Category>(this, 
				android.R.layout.simple_spinner_item, categories);
		
		catSpinner.setAdapter(dataAdapter);
		db.close();
	}

}
