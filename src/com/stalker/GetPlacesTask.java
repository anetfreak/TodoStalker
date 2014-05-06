package com.stalker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.AsyncTask;

import com.stalker.DBHelper.DatabaseHandler;
import com.stalker.DBHelper.Todo;
import com.stalker.places.PlacesList;
import com.stalker.places.PlacesUtil;

public class GetPlacesTask extends AsyncTask<Void, Void, String>{

	public static Map<Todo,PlacesList> TODOtoPlaces;
	private Context mContext;
	
	public GetPlacesTask(Context context){
		this.mContext = context;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}

	@Override
	protected String doInBackground(Void... params) {
		if(TODOtoPlaces!=null)
			TODOtoPlaces.clear();
		else
			TODOtoPlaces = new HashMap<Todo, PlacesList>();
		DatabaseHandler db = new DatabaseHandler(mContext);
		List<Todo> todos = new ArrayList<Todo>();
		todos = db.getAllTodos();
		PlacesUtil p = new PlacesUtil();
		PlacesList todoPlaces;
		for (Todo todo : todos) {
			todoPlaces = new PlacesList();
			todoPlaces = p.getNearPlaces(todo);
			TODOtoPlaces.put(todo, todoPlaces);
		}
		return null;
	}

}