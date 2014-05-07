package com.stalker;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

public class MainActivity extends Activity {

	public static final String PREFS_NAME = "TodoStalkerPref";
	
	@Override
	protected void onStart() {
		super.onStart();
		Switch notifySwitch = (Switch) findViewById(R.id.notifySwitch);
		notifySwitch.setChecked(true);

		SharedPreferences prefData = getSharedPreferences(PREFS_NAME, 0);
		boolean notifyPref = prefData.getBoolean("Notify", true);
		if(!notifyPref) {
			notifySwitch.setChecked(false);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Switch notifySwitch = (Switch) findViewById(R.id.notifySwitch);
		notifySwitch.setChecked(true);

		SharedPreferences prefData = getSharedPreferences(PREFS_NAME, 0);
		boolean notifyPref = prefData.getBoolean("Notify", true);
		if(!notifyPref) {
			notifySwitch.setChecked(false);
		}
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	// Function to update the preferences of the app
	public void updatePref(View v) {
		
		SharedPreferences prefData = getSharedPreferences(PREFS_NAME, 0);
		
		Switch notifySwitch = (Switch) findViewById(R.id.notifySwitch);
		if(notifySwitch.isChecked()){
			//Notifications to be enabled
			SharedPreferences.Editor editor = prefData.edit();
			editor.putBoolean("Notify", true);
			editor.commit();
		} else {
			//Notifications to be disabled
			SharedPreferences.Editor editor = prefData.edit();
			editor.putBoolean("Notify", false);
			editor.commit();
		}
	}
	
}
