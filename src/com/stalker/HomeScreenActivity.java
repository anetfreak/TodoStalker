package com.stalker;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.stalker.DBHelper.DatabaseHandler;
import com.stalker.places.PlacesList;

public class HomeScreenActivity extends FragmentActivity implements GooglePlayServicesClient.ConnectionCallbacks, 
GooglePlayServicesClient.OnConnectionFailedListener {

	private Button btnMap;
	private Button btnService;
	private Button btnList;
	private Button btnAdd;
	private TextView percentTV;

	public static PlacesList nearMe;
//	public static Map<Todo,PlacesList> TODOtoPlaces;
	public static double currentLatitude;
	public static double currentLongitude;



	LocationClient locationClient;
	Intent intentService;
	PendingIntent pendingIntent;
	boolean serviceCreated = false;
	LocationRequest locationRequest;
	PieGraph pg;
	
	@Override
	protected void onStart() {
		super.onStart();
		locationClient.connect();
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		pg = (PieGraph) findViewById(R.id.graph);
		pg.removeSlices();
        PieSlice slice1 = new PieSlice();
        slice1.setColor(Color.parseColor("#FFFF33"));
        float all = db.getAllTodos().size();
        float done = db.getAllTodos().size()-db.getAllUndoneTodos().size();
        float undone = db.getAllUndoneTodos().size();
        slice1.setValue(done);
        pg.addSlice(slice1);
        PieSlice slice2 = new PieSlice();
        slice2.setColor(Color.parseColor("#669900"));
        slice2.setValue(undone);
        pg.addSlice(slice2);
        db.closeDB();
        Float percent = (done / all) * 100;
        percent = (float) (Math.round(percent*100.0) / 100.0);
        if(percent.isNaN())
        	percent = (float) 0;
        Log.i("percent ", Float.toString(percent));
        percentTV.setText(Float.toString(percent) + "%");
	}
	
	@Override
	protected void onStop() {
		locationClient.disconnect();
		super.onStop();
	}
	
	
	@Override
	public void onConnected(Bundle dataBundle) {
//		displayCurrentLocation();
		
		if(!serviceCreated) {
			intentService = new Intent(this, LocationService.class);
			pendingIntent = PendingIntent.getService(this, 1, intentService, 0);
			locationRequest = LocationRequest.create();
			
//			//Setting a fastest and normal interval to 5min and 30min
//			locationRequest.setInterval(1800000);
//			locationRequest.setFastestInterval(300000);
//			//Setting the distance after which the service should check the location updates to 500meters.
//			locationRequest.setSmallestDisplacement(500);
			
			//Testing
			locationRequest.setInterval(1800000);
			
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
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_screen);
		locationClient = new LocationClient(this, this, this);

		findViewById(R.id.fullscreen_content_controls);
		final View controlsView = findViewById(R.id.fullscreen_content_controls);

		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.


		final Intent mapIntent = new Intent(getApplicationContext(), MapAllTODOs.class);
		final Intent addIntent = new Intent(getApplicationContext(), AddToDoActivity.class);
		final Intent listIntent = new Intent(getApplicationContext(), ListTodoActivity.class);
		final Intent settingsIntent = new Intent(getApplicationContext(), MainActivity.class);

		btnAdd = (Button) findViewById(R.id.Button01);
		btnMap = (Button) findViewById(R.id.Button02);
		btnService = (Button) findViewById(R.id.button2);
		btnList = (Button) findViewById(R.id.button1);
		percentTV = (TextView) findViewById(R.id.percent);

		btnAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(addIntent);
			}
		});

		btnMap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(mapIntent);
			}
		});

		btnService.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(settingsIntent);
			}
		});
		
		btnList.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(listIntent);
			}
		});
//        if(nearMe==null){
//        	(new GetPlacesTask()).execute();
//        }

	}

}