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
			if(HomeScreenActivity.TODOtoPlaces != null) {
				for(Entry<Todo, PlacesList> todoMap : HomeScreenActivity.TODOtoPlaces.entrySet()) {
					if(todoMap.getKey().getStatus() == 0) {
						for(int i = 0; i < todoMap.getValue().results.size(); i++) {
							System.out.println("Number of places for " + todoMap.getKey().getNote() + " todo are " + todoMap.getValue().results.size());
							
							Location placeLoc = new Location("Place Location");
							placeLoc.setLatitude(todoMap.getValue().results.get(i).geometry.location.lat);
							placeLoc.setLongitude(todoMap.getValue().results.get(i).geometry.location.lng);
							float distance = todoLoc.distanceTo(placeLoc);
							System.out.println("Distance between current location and todo place item - " + distance + "m");
							
							if(distance <= 500) {
								nearbyTodos.add(todoMap.getKey());
								break;
							}
						}
					}
				}
				
				//Check if there are any nearby todos that the user can go and resolve.
				System.out.println("Nearby Todo items to be notified for " + nearbyTodos.size());
				if( nearbyTodos.size() > 0 ) {
					//Send a notification to the user.
					
					Intent intentNotify = new Intent(this, HomeScreenActivity.class);
					PendingIntent notifyPendingIntent = PendingIntent.getActivity(this, 1, intentNotify, 0);
					StringBuilder content = new StringBuilder();
					NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
			        String[] todos = new String[5];
			        for(int i=0; i<nearbyTodos.size() && i < 5; i++) {
//			        	content.append(nearbyTodos.get(i).getNote().trim() + "\n");
			        	todos[i] = (i+1) + ". " + nearbyTodos.get(i).getNote().trim();
			        }
			        
					System.out.println("Number of todo events - " + todos.length);
					for(int j=0; j < 3; j++) {
//						inboxStyle.addLine(content.append(nearbyTodos.get(j).getNote().trim()));
						inboxStyle.addLine(todos[j]);
					}
					
					String notificationText = nearbyTodos.size() + " of your todo tasks are in your vicinity..";
//					String notificationText = content.toString();
					myNotification = new NotificationCompat.Builder(getApplicationContext())
					.setContentTitle("TODO Alert..")
					.setContentText(notificationText)
					.setTicker("TodoStalker tasks are in your vicinity..")
					.setWhen(System.currentTimeMillis())
					.setDefaults(Notification.DEFAULT_SOUND)
					.setAutoCancel(true)
					.setSmallIcon(R.drawable.stalker)
					.setContentIntent(notifyPendingIntent)
					.setStyle(inboxStyle)
					.build();

					notificationManager.notify(NOTIFICATION_ID, myNotification);
				}
			}
		}
	}
}