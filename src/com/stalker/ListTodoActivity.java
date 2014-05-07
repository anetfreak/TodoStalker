package com.stalker;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.stalker.R;
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
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.stalker.DBHelper.DatabaseHandler;
import com.stalker.DBHelper.Todo;

public class ListTodoActivity extends Activity implements OnItemSelectedListener{
	DatabaseHandler myDB;
	int icon = R.drawable.map1;
	String categoryName;
	Cursor cursor;
	Spinner spinner1;
	Spinner spinner2;
	private Button btnAdd;
	String selcat="All";
	String selDate="All";
	CheckBox checkbox;

	private final String [] categories = new String[] 
			{"Shopping","Food & Drink","Travel","Home","Health & Medicine",
			"Bank/ATM","Fuel","Study","Entertainment","Other"};

	private final String[] colors = new String[] 
			{"#33B5E5","#AA66CC","#99CC00","#FFBB33","#FF4444","#0099CC","#9933CC","#FFD700","#FF8800","#CC0000"};
	Map<String, String> catColor = new HashMap<String, String>();

	private final String [] categoryArray = new String[] 
			{"All","Shopping","Food & Drink","Travel","Home","Health & Medicine",
			"Bank/ATM","Fuel","Study","Entertainment","Other"};


	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_todo);
		
		checkbox = (CheckBox) findViewById(R.id.checkBox1);
		spinner1 = (Spinner)findViewById(R.id.spinner1);
		spinner2 = (Spinner)findViewById(R.id.spinner2);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(ListTodoActivity.this,
				android.R.layout.simple_spinner_item,categoryArray);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner1.setAdapter(adapter);
		spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position,
					long id) {
				spinner1.setSelection(position);
				selcat = (String) spinner1.getSelectedItem();
				populateTodoListFromDB(selcat,selDate);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position,
					long id) {
				spinner2.setSelection(position);
				selDate = (String) spinner2.getSelectedItem();
				populateTodoListFromDB(selcat,selDate);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		openDB();
		loadSpinnerData();

		populateHashMap();

		populateTodoListFromDB(selcat,selDate);

		final Intent addIntent = new Intent(getApplicationContext(), AddToDoActivity.class);

		btnAdd = (Button) findViewById(R.id.button1);

		btnAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(addIntent);
			}
		});
		
	}

	private void loadSpinnerData() {
		List<Todo> dates = myDB.getAllTodos();
		List<String> dateArray = new ArrayList<String>();
		dateArray.add("All");
		for(int i = 0; i < dates.size(); i++ ){
			String stDate = dates.get(i).getStartDate();
			dateArray.add(stDate);
		}
		ArrayAdapter<String> dateAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, dateArray);
		dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner2.setAdapter(dateAdapter);
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


	private void populateTodoListFromDB(String category, String date) {

		if((category.equalsIgnoreCase("All")) && (date.equalsIgnoreCase("All"))){

			cursor = myDB.getAllRows();
		}else if ((category.equalsIgnoreCase("All"))) {
			cursor = myDB.getAllRowsDat(date);
		}else if (date.equalsIgnoreCase("All")) {
			cursor = myDB.getAllRowsCat(category);
		}else{
			cursor = myDB.getAllRowsCatDate(category,date);
		}

		startManagingCursor(cursor);

		//Setup mapping from cursor to view fields
		String[] fieldNames = new String[]{
				DatabaseHandler.KEY_NOTE, DatabaseHandler.Id};

		int[] viewIds = new int[] {R.id.item_todo,R.id.percent};


		//Create Adapter to map columns of DB to the view
		SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, R.layout.icon_view, cursor, fieldNames, viewIds){

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {


				final View row = super.getView(position, convertView, parent);

				CheckBox statusBox = (CheckBox) row.findViewById(R.id.checkBox1);
				cursor.moveToPosition(position);
				String category = cursor.getString(2);
				String status = cursor.getString(5);
				Log.i("Status ", status);
				if(status.equalsIgnoreCase("Done"))
					statusBox.setChecked(true);
				else
					statusBox.setChecked(false);
				
				Log.i("ct", category);
				Log.i("rowId rr ", cursor.getString(0));
				row.setBackgroundColor(Color.parseColor(catColor.get(category)));
				
//				if((position % 2) == 0){
//					row.setBackgroundColor(Color.parseColor("#33B5E5"));
//				}else{
//					row.setBackgroundColor(Color.parseColor("#70CBED"));
//				}
				
				return row;
			}
		};

		ListView list = (ListView) findViewById(R.id.notificationsList);
		list.setAdapter(cursorAdapter);
	}	

	public void viewMap(View view){
		// display todo location on map
		Intent i = new Intent(getApplicationContext(),MapAllTODOs.class);

		View parentView = (View) view.getParent();
		String rowIdVal   = ((TextView) parentView.findViewById(R.id.percent)).getText().toString();
		Log.i("rowIdval", rowIdVal);
		i.putExtra("identifier", Integer.valueOf(rowIdVal));
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
	}
	public void checkboxClicked(View view){
		String status;
		 boolean checked = ((CheckBox) view).isChecked();
		 View parentView1 = (View) view.getParent();
			String rowIdVal1   = ((TextView) parentView1.findViewById(R.id.percent)).getText().toString();
		 if(checked){
			// checkbox.setChecked(true);
			
			status = "Done";
			 myDB.updateStatus(rowIdVal1,status);
			 
			 Toast.makeText(ListTodoActivity.this,
				 	   "checked :)", Toast.LENGTH_LONG).show();
		 }else{
			 status = "UnDone";
			 myDB.updateStatus(rowIdVal1,status);
			 Toast.makeText(ListTodoActivity.this,
				 	   "unchecked :)", Toast.LENGTH_LONG).show();
		 }
		 populateTodoListFromDB(selcat, selDate);
	}

	public void deleteTodo(View view){
		View parentView = (View) view.getParent();
		String rowIdVal   = ((TextView) parentView.findViewById(R.id.percent)).getText().toString();
		myDB.deleteTodo(rowIdVal);
		populateTodoListFromDB(selcat, selDate);
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		//		spinner1.setSelection(position);
		//		String selState = (String) spinner1.getSelectedItem();
		//		populateTodoListFromDB(selState);

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}
	

}
