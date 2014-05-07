package com.stalker;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends Activity {

	public static final String PREFS_NAME = "TodoStalkerPref";
	EditText radius;
	Switch notifySwitch;
	
	@Override
	protected void onStart() {
		super.onStart();
		notifySwitch.setChecked(true);

		SharedPreferences prefData = getSharedPreferences(PREFS_NAME, 0);
		boolean notifyPref = prefData.getBoolean("Notify", true);
		if(!notifyPref) {
			notifySwitch.setChecked(false);
		}
		int savedRadius = prefData.getInt("Radius", 500);
		radius.setText(String.valueOf(savedRadius));
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		notifySwitch.setChecked(true);

		SharedPreferences prefData = getSharedPreferences(PREFS_NAME, 0);
		boolean notifyPref = prefData.getBoolean("Notify", true);
		if(!notifyPref) {
			notifySwitch.setChecked(false);
		}
		
		int savedRadius = prefData.getInt("Radius", 500);
		radius.setText(String.valueOf(savedRadius));
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		radius = (EditText) findViewById(R.id.textRadiusInput);
		notifySwitch = (Switch) findViewById(R.id.notifySwitch);
	}
	// Function to update the preferences of the app
	public void updatePref(View v) {
		
		SharedPreferences prefData = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = prefData.edit();
		
		if(notifySwitch.isChecked()){
			//Notifications to be enabled
			
			editor.putBoolean("Notify", true);
			editor.commit();
		} else {
			//Notifications to be disabled
			editor.putBoolean("Notify", false);
			editor.commit();
		}
		
		editor.putInt("Radius", Integer.valueOf(radius.getText().toString()));
		editor.commit();
		
		Toast.makeText(getApplicationContext(), "Preferences updated", Toast.LENGTH_SHORT).show();
		finish();
	}
	
}
