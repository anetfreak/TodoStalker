package com.stalker;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;

public class MainActivity extends FragmentActivity implements GooglePlayServicesClient.ConnectionCallbacks, 
GooglePlayServicesClient.OnConnectionFailedListener {

	LocationClient locationClient;
	private TextView addressLabel;
	Intent intentService;
	PendingIntent pendingIntent;
	boolean serviceCreated = false;
	LocationRequest locationRequest;
	
	@Override
	protected void onStart() {
		super.onStart();
		locationClient.connect();
	}
	
	@Override
	protected void onStop() {
		locationClient.disconnect();
		super.onStop();
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		locationClient = new LocationClient(this, this, this);
		addressLabel = (TextView) findViewById(R.id.addressLabel);
	}
	
	@Override
	public void onConnected(Bundle dataBundle) {
		displayCurrentLocation();
		
		if(!serviceCreated) {
			intentService = new Intent(this, LocationService.class);
			pendingIntent = PendingIntent.getService(this, 1, intentService, 0);
			locationRequest = LocationRequest.create();
			locationRequest.setInterval(3000);
			locationClient.requestLocationUpdates(locationRequest, pendingIntent);
			serviceCreated = true;
			startService(intentService);
		}
	}
	
	@Override
	public void onDisconnected() {
		Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		Toast.makeText(this, "Connection Failure : " + connectionResult.getErrorCode(), Toast.LENGTH_SHORT).show();
	}
	
	public void displayCurrentLocation() {
		// Get the current location's latitude & longitude
		if(locationClient != null && locationClient.isConnected()) {
			Location currentLocation = locationClient.getLastLocation();
			if(currentLocation != null) {
				//Displays the address in the UI..
				(new GetAddressTask(this)).execute(currentLocation);
			} else {
				Toast.makeText(this, "Error retreiving current location.. Trying again..", Toast.LENGTH_SHORT).show();
			}
		}
	}

	
	//Does the task of fetching the location based on the current position..
	private class GetAddressTask extends AsyncTask<Location, Void, String>{
		Context mContext;
		public GetAddressTask(Context context) {
			super();
			mContext = context;
		}

		@Override
		protected void onPostExecute(String address) {
			addressLabel.setText(address);
		}
		@Override
		protected String doInBackground(Location... params) {
			Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
			Location loc = params[0];

			// Create a list to contain the result address
			List<Address> addresses = null;
			try {
				addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
			} catch (IOException e1) {
				Log.e("MainActivity", "IO Exception in getFromLocation()");
				e1.printStackTrace();
				return ("IO Exception trying to get address");
			} catch (IllegalArgumentException e2) {
				String errorString = "Illegal arguments " + Double.toString(loc.getLatitude()) + " , " + Double.toString(loc.getLongitude()) + " passed to address service";
				Log.e("MainActivity", errorString);
				e2.printStackTrace();
				return errorString;
			}

			if (addresses != null && addresses.size() > 0) {
				Address address = addresses.get(0);
				String addressText = String.format("%s, %s, %s", address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
						address.getLocality(), address.getCountryName());
				return addressText;
			} else {
				return "Address not found";
			}
		}
	}
}
