package com.stalker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.stalker.MapAllTODOs.DisplayOnMap;
import com.stalker.DBHelper.DatabaseHandler;
import com.stalker.DBHelper.Todo;
import com.stalker.places.PlacesList;
import com.stalker.places.PlacesUtil;
import com.stalker.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;

import android.app.PendingIntent;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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
import com.stalker.places.PlacesList;
import com.stalker.places.PlacesUtil;
import com.stalker.util.SystemUiHider;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class HomeScreenActivity extends FragmentActivity implements GooglePlayServicesClient.ConnectionCallbacks, 
GooglePlayServicesClient.OnConnectionFailedListener {
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
//	private SystemUiHider mSystemUiHider;

	private Button btnMap;
	private Button btnService;
	private Button btnList;
	private Button btnAdd;


	public static PlacesList nearMe;
	public static Map<Todo,PlacesList> TODOtoPlaces;
	public static double currentLatitude;
	public static double currentLongitude;



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
		(new GetPlacesTask()).execute();
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
			
			//Setting a fastest and normal interval to 5min and 30min
			locationRequest.setInterval(1800000);
			locationRequest.setFastestInterval(300000);
			//Setting the distance after which the service should check the location updates to 500meters.
			locationRequest.setSmallestDisplacement(500);
			
			//Testing
//			locationRequest.setInterval(5000);
			
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
//		addressLabel = (TextView) findViewById(R.id.addressLabel);

		final View controlsView = findViewById(R.id.fullscreen_content_controls);

		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.

//		mSystemUiHider.setup();
//		mSystemUiHider.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
//					// Cached values.
//					int mControlsHeight;
//					int mShortAnimTime;
//
//					@Override
//					@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
//					public void onVisibilityChange(boolean visible) {
//						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
//							// If the ViewPropertyAnimator API is available
//							// (Honeycomb MR2 and later), use it to animate the
//							// in-layout UI controls at the bottom of the
//							// screen.
//							if (mControlsHeight == 0) {
//								mControlsHeight = controlsView.getHeight();
//							}
//							if (mShortAnimTime == 0) {
//								mShortAnimTime = getResources().getInteger(
//										android.R.integer.config_shortAnimTime);
//							}
//							controlsView
//									.animate()
//									.translationY(visible ? 0 : mControlsHeight)
//									.setDuration(mShortAnimTime);
//						} else {
//							// If the ViewPropertyAnimator APIs aren't
//							// available, simply show or hide the in-layout UI
//							// controls.
//							controlsView.setVisibility(visible ? View.VISIBLE
//									: View.GONE);
//						}
//
//						if (visible && AUTO_HIDE) {
//							// Schedule a hide().
//							delayedHide(AUTO_HIDE_DELAY_MILLIS);
//						}
//					}
//				});

		// Set up the user interaction to manually show or hide the system UI.

		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.

		final Intent mapIntent = new Intent(getApplicationContext(), MapAllTODOs.class);
		final Intent addIntent = new Intent(getApplicationContext(), AddToDoActivity.class);
		final Intent listIntent = new Intent(getApplicationContext(), ListTodoActivity.class);
		final Intent servIntent = new Intent(getApplicationContext(), MainActivity.class);

		btnAdd = (Button) findViewById(R.id.Button01);
		btnMap = (Button) findViewById(R.id.Button02);
		btnService = (Button) findViewById(R.id.button2);
		btnList = (Button) findViewById(R.id.button1);

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
				startActivity(servIntent);
			}
		});
		
		btnList.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(listIntent);
			}
		});

		PieGraph pg = (PieGraph)findViewById(R.id.graph);
        PieSlice slice = new PieSlice();
        slice.setColor(Color.parseColor("#99CC00"));
        slice.setValue(2);
        pg.addSlice(slice);
        slice = new PieSlice();
        slice.setColor(Color.parseColor("#FFBB33"));
        slice.setValue(8);
        pg.addSlice(slice);
        
        if(nearMe==null){
        	(new GetPlacesTask()).execute();
        }

	}

//	@Override
//	protected void onPostCreate(Bundle savedInstanceState) {
//		super.onPostCreate(savedInstanceState);
//
//		// Trigger the initial hide() shortly after the activity has been
//		// created, to briefly hint to the user that UI controls
//		// are available.
//		delayedHide(100);
//	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
//	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
//		@Override
//		public boolean onTouch(View view, MotionEvent motionEvent) {
//			if (AUTO_HIDE) {
//				delayedHide(AUTO_HIDE_DELAY_MILLIS);
//			}
//			return false;
//		}
//	};

//	Handler mHideHandler = new Handler();
//	Runnable mHideRunnable = new Runnable() {
//		@Override
//		public void run() {
//			mSystemUiHider.hide();
//		}
//	};

	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
//	private void delayedHide(int delayMillis) {
//		mHideHandler.removeCallbacks(mHideRunnable);
//		mHideHandler.postDelayed(mHideRunnable, delayMillis);
//	}

	public class GetPlacesTask extends AsyncTask<Void, Void, String>{

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Toast.makeText(getApplicationContext(), "DONE", 100).show();;
		}

		@Override
		protected String doInBackground(Void... params) {
			if(TODOtoPlaces!=null)
				TODOtoPlaces.clear();
			else
				TODOtoPlaces = new HashMap<Todo, PlacesList>();
			DatabaseHandler db = new DatabaseHandler(getApplicationContext());
			List<Todo> todos = new ArrayList<Todo>();
			todos = db.getAllTodos();
			PlacesUtil p = new PlacesUtil();
			PlacesList todoPlaces;
			for (Todo todo : todos) {
				todoPlaces = new PlacesList();
				todoPlaces = p.getNearPlaces(todo);
				TODOtoPlaces.put(todo, todoPlaces);
			}
			return null;
		}

	}
	
//	public void displayCurrentLocation() {
//		// Get the current location's latitude & longitude
//		if(locationClient != null && locationClient.isConnected()) {
//			Location currentLocation = locationClient.getLastLocation();
//			if(currentLocation != null) {
//				//Displays the address in the UI..
//				(new GetAddressTask(this)).execute(currentLocation);
//			} else {
//				Toast.makeText(this, "Error retreiving current location.. Trying again..", Toast.LENGTH_SHORT).show();
//			}
//		}
//	}
//
//	
//	//Does the task of fetching the location based on the current position..
//	private class GetAddressTask extends AsyncTask<Location, Void, String>{
//		Context mContext;
//		public GetAddressTask(Context context) {
//			super();
//			mContext = context;
//		}
//
//		@Override
//		protected void onPostExecute(String address) {
//			addressLabel.setText(address);
//		}
//		@Override
//		protected String doInBackground(Location... params) {
//			Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
//			Location loc = params[0];
//
//			// Create a list to contain the result address
//			List<Address> addresses = null;
//			try {
//				addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
//			} catch (IOException e1) {
//				Log.e("MainActivity", "IO Exception in getFromLocation()");
//				e1.printStackTrace();
//				return ("IO Exception trying to get address");
//			} catch (IllegalArgumentException e2) {
//				String errorString = "Illegal arguments " + Double.toString(loc.getLatitude()) + " , " + Double.toString(loc.getLongitude()) + " passed to address service";
//				Log.e("MainActivity", errorString);
//				e2.printStackTrace();
//				return errorString;
//			}
//
//			if (addresses != null && addresses.size() > 0) {
//				Address address = addresses.get(0);
//				String addressText = String.format("%s, %s, %s", address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
//						address.getLocality(), address.getCountryName());
//				return addressText;
//			} else {
//				return "Address not found";
//			}
//		}
//	}
}