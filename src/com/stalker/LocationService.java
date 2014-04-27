package com.stalker;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.location.Location;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.location.LocationClient;

public class LocationService extends IntentService {

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
			System.out.println("Location is null..");
			//TODO Do the stuff you want to do when the location changes..
			
			//Sample Notification
//			NotificationCompat.Builder notBuild= new NotificationCompat.Builder(this);
//	        notBuild.setSmallIcon(R.drawable.ic_launcher);
//	        notBuild.setContentText("Location Changed..");
//	        notBuild.setContentTitle("Todo Stalker");
//	        int notBuildIdentifier=001;
//
//	        NotificationManager notMan = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//	        notMan.notify(notBuildIdentifier,notBuild.build());
		}
	}
}