package com.stalker;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.stalker.places.*;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.stalker.util.SystemUiHider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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

	private GoogleMap gMap;
	private List<MarkerOptions> markers;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_map_all_todos);

		try {
			markers = new ArrayList<MarkerOptions>();
			initializeMap();
			displayOnMap();
			gMap.setOnMarkerClickListener((OnMarkerClickListener) this);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.

	}

	private void displayOnMap() {
		if (HomeScreenActivity.nearMe != null)
			(new DisplayOnMap()).execute();
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

	}

	public class DisplayOnMap extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			for (MarkerOptions m : markers) {
				gMap.addMarker(m);
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			for (Place p : HomeScreenActivity.nearMe.results) {
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
				mo.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
				markers.add(mo);
				// gMap.addMarker(mo);
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
		return true;
	}

}
