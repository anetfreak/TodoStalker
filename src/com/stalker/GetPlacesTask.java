package com.stalker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
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
	public static boolean doNotModify = false;
	
	private long updateTime = 0;
	private long toUpdtaeTime = 0;
	
	public GetPlacesTask(Context context){
		this.mContext = context;
		//inProgress = false;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		doNotModify = false;
	}

	@Override
	protected String doInBackground(Void... params) {
		if(updateTime==0 || toUpdtaeTime<=System.currentTimeMillis()){
			updateTime = System.currentTimeMillis();
			if(TODOtoPlaces!=null)
				TODOtoPlaces.clear();
			else
				TODOtoPlaces = new Hashtable<Todo, PlacesList>();
			DatabaseHandler db = new DatabaseHandler(mContext);
			List<Todo> todos = new ArrayList<Todo>();
			todos = db.getAllUndoneTodos();
			db.closeDB();
			PlacesUtil p = new PlacesUtil();
			PlacesList todoPlaces;
			for (Todo todo : todos) {
				todoPlaces = new PlacesList();
				todoPlaces = p.getNearPlaces(todo);
				TODOtoPlaces.put(todo, todoPlaces);
			}
			
			toUpdtaeTime = updateTime + 300000;
		}
		return null;
	}

}