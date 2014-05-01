package com.stalker;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.location.LocationClient;

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
			
			Intent intentNotify = new Intent(this, HomeScreenActivity.class);
			PendingIntent notifyPendingIntent = PendingIntent.getActivity(this, 1, intentNotify, 0);
			
			String notificationText = "One or more of your todo's can be completed from a nearby location.";
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