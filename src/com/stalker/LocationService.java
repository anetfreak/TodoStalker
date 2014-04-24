package com.stalker;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
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
	protected void onHandleIntent(Intent intent) {
		System.out.println("Inside handleIntent..");
		Location location = intent.getParcelableExtra(LocationClient.KEY_LOCATION_CHANGED);
		if(location != null) {
			System.out.println("Location is not null..");
			//TODO Do the stuff you want to do when the location changes..
			
			//Sample Notification
//			NotificationCompat.Builder notBuild= new NotificationCompat.Builder(this);
//	        notBuild.setSmallIcon(R.drawable.ic_launcher);
//	        notBuild.setContentText("Location Changed..");
//	        notBuild.setContentTitle("Todo Stalker");
//
//	        NotificationManager notMan = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//	        notMan.notify(1,notBuild.build());
			
			System.out.println("Generating Notification..");
			String notificationText = String.valueOf((int)(100)) + " %";
			myNotification = new NotificationCompat.Builder(getApplicationContext())
			.setContentTitle("Progress")
			.setContentText(notificationText)
			.setTicker("Notification!")
			.setWhen(System.currentTimeMillis())
			.setDefaults(Notification.DEFAULT_SOUND)
			.setAutoCancel(true)
			.setSmallIcon(R.drawable.ic_launcher)
			.build();

			notificationManager.notify(NOTIFICATION_ID, myNotification);
			
		}
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		System.out.println("Inside onCreate of IntentService..");
	}
}