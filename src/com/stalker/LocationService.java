package com.stalker;

import java.util.ArrayList;
import java.util.Map.Entry;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.location.LocationClient;
import com.stalker.DBHelper.Todo;
import com.stalker.places.PlacesList;

public class LocationService extends IntentService {

	private static final int NOTIFICATION_ID=1;
	NotificationManager notificationManager;
	Notification myNotification;
	
	public LocationService() {
		super("Fused Location Service");
	}
	
	public LocationService(String name) {
		super("Fused Location Service");
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Location location = intent.getParcelableExtra(LocationClient.KEY_LOCATION_CHANGED);
		if(location != null) {
			//TODO Do the stuff you want to do when the location changes..
		
			System.out.println("Latitude now is " + location.getLatitude());
			System.out.println("Longitude now is " + location.getLongitude());
			HomeScreenActivity.currentLatitude = location.getLatitude();
			HomeScreenActivity.currentLongitude = location.getLongitude();
			
			//Check for distance and accordingly notify the user here...
			//Fetch all location from databases and compare here
			Location todoLoc = new Location("Current Location");
			todoLoc.setLatitude(location.getLatitude());
			todoLoc.setLongitude(location.getLongitude());
			
			ArrayList<Todo> nearbyTodos = new ArrayList<Todo>();
			for(Entry<Todo, PlacesList> todoMap : HomeScreenActivity.TODOtoPlaces.entrySet()) {
				for(int i = 0; i < todoMap.getValue().results.size(); i++) {
					
					Location placeLoc = new Location("Place Location");
					todoLoc.setLatitude(todoMap.getValue().results.get(i).geometry.location.lat);
					todoLoc.setLongitude(todoMap.getValue().results.get(i).geometry.location.lng);
					float distance = todoLoc.distanceTo(placeLoc);
					if(distance <= 500) {
						nearbyTodos.add(todoMap.getKey());
					}
				}
			}
			//Check if there are any nearby todos that the user can go and resolve.
			if( nearbyTodos.size() > 0 ) {
				//Send a notification to the user.
				
				Intent intentNotify = new Intent(this, HomeScreenActivity.class);
				PendingIntent notifyPendingIntent = PendingIntent.getActivity(this, 1, intentNotify, 0);
				
				String notificationText = nearbyTodos.size() + " of your todo tasks are in your vicinity..";
				myNotification = new NotificationCompat.Builder(getApplicationContext())
				.setContentTitle("TodoStalker")
				.setContentText(notificationText)
				.setTicker("TodoStalker")
				.setWhen(System.currentTimeMillis())
				.setDefaults(Notification.DEFAULT_SOUND)
				.setAutoCancel(true)
				.setSmallIcon(R.drawable.stalker)
				.setContentIntent(notifyPendingIntent)
				.build();

				notificationManager.notify(NOTIFICATION_ID, myNotification);
			}
			
		}
	}
}