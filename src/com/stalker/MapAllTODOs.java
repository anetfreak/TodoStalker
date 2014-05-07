package com.stalker;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.stalker.DBHelper.Todo;
import com.stalker.places.*;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.stalker.util.SystemUiHider;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class MapAllTODOs extends Activity implements OnMarkerClickListener {
	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = true;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

	/**
	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
	 * will show the system UI visibility upon interaction.
	 */
	private static final boolean TOGGLE_ON_CLICK = true;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;

	public static final String[] categories = new String[] { "Shopping",
			"Food & Drink", "Travel", "Home", "Health & Medicine", "Bank/ATM",
			"Fuel", "Study", "Entertainment", "Other" };
	public static final float[] colors = new float[] {
			BitmapDescriptorFactory.HUE_AZURE,
			BitmapDescriptorFactory.HUE_BLUE, BitmapDescriptorFactory.HUE_CYAN,
			BitmapDescriptorFactory.HUE_GREEN,
			BitmapDescriptorFactory.HUE_MAGENTA,
			BitmapDescriptorFactory.HUE_ORANGE,
			BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_ROSE,
			BitmapDescriptorFactory.HUE_VIOLET,
			BitmapDescriptorFactory.HUE_YELLOW };

	
	public static final int[] icons = new int[]{
		R.drawable.shopping_bag,
		R.drawable.food_and_drink,
		R.drawable.travel,
		R.drawable.home,
		R.drawable.health_medicine,
		R.drawable.bank,
		R.drawable.fuel,
		R.drawable.study,
		R.drawable.work,
		R.drawable.other
	};
	
	public static Map<String, Integer> catColor = new HashMap<String, Integer>();
	
	public static Map<String,Place> markerToPlace = new HashMap<String, Place>();


	private void populateMarkerMap() {
		int i = 0;
		for (String cat : categories) {
			catColor.put(cat, icons[i]);
			i++;
		}
	}

	private GoogleMap gMap;
	private Map<MarkerOptions,Place> markers;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_map_all_todos);

		try {
			markers = new HashMap<MarkerOptions,Place>();

			populateMarkerMap();
			initializeMap();
			Intent i = getIntent();
			Integer todoID;
			if(i.hasExtra("identifier")){
				todoID = i.getIntExtra("identifier", -1);
				displayOnMap(todoID);
			}
			else{
				displayOnMap(-2);
			}
			 
			
			gMap.setOnMarkerClickListener((OnMarkerClickListener) this);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.

	}

	private void displayOnMap(Integer todoID) {
//		while(GetPlacesTask.inProgress){
//			try {
//				Thread.sleep(200);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		if (GetPlacesTask.TODOtoPlaces != null)
			
			(new DisplayOnMap()).execute(todoID);
	}

	private void initializeMap() {
		if (gMap == null) {
			gMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.mapTodo)).getMap();
			gMap.moveCamera(CameraUpdateFactory
					.newCameraPosition(new CameraPosition(new LatLng(
							37.3357190, -121.8867080), 14, 0, 0)));

			if (gMap == null) {
				Toast.makeText(getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
			}
			
			 
		}
		populateMarkerMap();
		
	}

	public class DisplayOnMap extends AsyncTask<Integer, Void, Void> {

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			GetPlacesTask.doNotModify = false;
			for (Map.Entry<MarkerOptions, Place> m : markers.entrySet()) {
				Marker mr = gMap.addMarker(m.getKey());
				System.out.println(mr.getId());
				markerToPlace.put(mr.getId(), m.getValue());
			}
			gMap.setInfoWindowAdapter(new InfoWindowAdapter() {

	            // Use default InfoWindow frame
	            @Override
	            public View getInfoWindow(Marker args) {
	                return null;
	            }

	            // Defines the contents of the InfoWindow
	            @Override
	            public View getInfoContents(Marker args) {

	                gMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {          
	                    public void onInfoWindowClick(Marker marker) 
	                    {
	                    	
	                    	// Creating an instance of DisplayMetrics
	                        DisplayMetrics dm = new DisplayMetrics();
	     
	                        // Getting the screen display metrics
	                        getWindowManager().getDefaultDisplay().getMetrics(dm);
	     
	                        // Creating a dialog fragment to display the photo
	                        ShowPlaceDialog dialogFragment = new ShowPlaceDialog(markerToPlace.get(marker.getId()),dm);
	     
	                        // Getting a reference to Fragment Manager
	                        FragmentManager fm = getFragmentManager();
	     
	                        // Starting Fragment Transaction
	                        FragmentTransaction ft = fm.beginTransaction();
	     
	                        // Adding the dialog fragment to the transaction
	                        ft.add(dialogFragment, "TAG");
	     
	                        // Committing the fragment transaction
	                        ft.commit();
	                    }
	                });

	                // Returning the view containing InfoWindow contents
	                return null;

	            }
	        }); 
		}

		@Override
		protected Void doInBackground(Integer... params) {
			Integer identifier = -2;
			try{
			if(params.length>0)
				identifier = params[0];

			if (identifier > -1) {
				GetPlacesTask.doNotModify = true;
				for (Map.Entry<Todo, PlacesList> todoTask : GetPlacesTask.TODOtoPlaces
						.entrySet()) {
					if(todoTask.getKey().getId()==identifier){

						createMarker(todoTask);
					}
				}
			}
			else if(identifier==-2){
				for (Map.Entry<Todo, PlacesList> todoTask : GetPlacesTask.TODOtoPlaces
						.entrySet()) {
					if(todoTask.getValue().results!=null){
						createMarker(todoTask);
					}
				}
			}
			}
			catch(Exception ex){
				System.out.println(ex.getMessage());
			}
			return null;
		}

		private void createMarker(Map.Entry<Todo, PlacesList> todoTask) {
			for (Place p : todoTask.getValue().results) {
				MarkerOptions mo = new MarkerOptions().position(
						new LatLng(p.geometry.location.lat,
								p.geometry.location.lng)).title(p.name);
				mo.icon(BitmapDescriptorFactory.fromResource(catColor.get(todoTask.getKey().getCategory())));
				//mo.icon(BitmapDescriptorFactory.defaultMarker(catColor
					//	.get(todoTask.getKey().getCategory())));
				p.associatedTODO = new Todo();
				p.associatedTODO = todoTask.getKey();
				markers.put(mo, p);
				
				// gMap.addMarker(mo);
			}
		}

	}
	
	public void onInfoWindowClick (Marker marker){
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		
		return false;
	}

}
