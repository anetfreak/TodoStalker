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
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.stalker.util.SystemUiHider;

import android.R.drawable;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * @param <dovesiamo>
 * 
 * @see SystemUiHider
 */
public class MapAllTODOs extends Activity implements OnMarkerClickListener {

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

	private void populateMarkerMap() {
		int i = 0;
		for (String cat : categories) {
			catColor.put(cat, icons[i]);
			i++;
		}
	}

	private GoogleMap gMap;
	private List<MarkerOptions> markers;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_map_all_todos);

		try {
			markers = new ArrayList<MarkerOptions>();
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
		if (HomeScreenActivity.TODOtoPlaces != null)
			
			(new DisplayOnMap()).execute(todoID);
	}

	private void initializeMap() {
		if (gMap == null) {
			gMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.mapTodo)).getMap();
			gMap.moveCamera(CameraUpdateFactory
					.newCameraPosition(new CameraPosition(new LatLng(HomeScreenActivity.currentLatitude,HomeScreenActivity.currentLongitude), 14, 0, 0)));

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
			for (MarkerOptions m : markers) {
				gMap.addMarker(m);
			}
		}

		@Override
		protected Void doInBackground(Integer... params) {
			Integer identifier = -2;
			if(params.length>0)
				identifier = params[0];

			if (identifier > -1) {
				for (Map.Entry<Todo, PlacesList> todoTask : HomeScreenActivity.TODOtoPlaces
						.entrySet()) {
					if(todoTask.getKey().getId()==identifier){
						for (Place p : todoTask.getValue().results) {
							MarkerOptions mo = new MarkerOptions().position(
									new LatLng(p.geometry.location.lat,
											p.geometry.location.lng)).title(p.name);
							Bitmap bmp = null;
							try {
								URL u = new URL(p.icon);
								bmp = BitmapFactory.decodeStream(u.openConnection()
										.getInputStream());
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							mo.icon(BitmapDescriptorFactory.fromResource(catColor.get(todoTask.getKey().getCategory())));
							markers.add(mo);
							// gMap.addMarker(mo);
						}
					}
				}
			}
			else if(identifier==-2){
				for (Map.Entry<Todo, PlacesList> todoTask : HomeScreenActivity.TODOtoPlaces
						.entrySet()) {
					if(todoTask.getValue().results!=null){
						for (Place p : todoTask.getValue().results) {
							MarkerOptions mo = new MarkerOptions().position(
									new LatLng(p.geometry.location.lat,
											p.geometry.location.lng)).title(p.name);
							Bitmap bmp = null;
							try {
								URL u = new URL(p.icon);
								bmp = BitmapFactory.decodeStream(u.openConnection()
										.getInputStream());
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							mo.icon(BitmapDescriptorFactory.fromResource(catColor.get(todoTask.getKey().getCategory())));
							//mo.icon(BitmapDescriptorFactory.defaultMarker(catColor
								//	.get(todoTask.getKey().getCategory())));
							markers.add(mo);
							// gMap.addMarker(mo);
						}
					}
				}
			}
			return null;
		}

	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
				Uri.parse("http://maps.google.com/maps?saddr="
						+ String.valueOf(HomeScreenActivity.currentLatitude)
						+ ","
						+ String.valueOf(HomeScreenActivity.currentLongitude)
						+ "&daddr="
						+ String.valueOf(marker.getPosition().latitude) + ","
						+ String.valueOf(marker.getPosition().longitude)));
		startActivity(intent);
		return false;
	}

}
