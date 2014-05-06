package com.stalker.places;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.stalker.HomeScreenActivity;
import com.stalker.DBHelper.DatabaseHandler;
import com.stalker.DBHelper.Todo;
import com.stalker.places.PlacesList;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.gson.Gson;

public class PlacesUtil {

	private Map<String, String> categoryToPlaceType;

	public static String types = "atm|bar";

	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

	// Google API Key
	private static final String API_KEY = "AIzaSyB7ylAhKS8C3j7EhvxkPERYz4AdwQbePDY";

	// Google Places serach url's
	private static final String PLACES_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";
	private static final String PLACES_TEXT_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/textsearch/json";
	private static final String PLACES_DETAILS_URL = "https://maps.googleapis.com/maps/api/place/details/json";

	private String _radius = "500";

	public static final String[] categories = new String[] { "Shopping",
			"Food & Drink", "Travel", "Home", "Health & Medicine", "Bank/ATM",
			"Fuel", "Study", "Entertainment", "Other" };
	public static final String[] gType = new String[] {"clothing_store|electronics_store|department_store|liquor_store|jewelry_store|shoe_store|shopping_mall|pet_store|bicycle_store",
														"bakery|bar|cafe|casino|food|meal_delivery|meal_takeaway|restaurant",
														"airport|lodging|taxi_stand|train_station|travel_agency|car_rental|subway_station",
														"home_goods_store|furniture_store|grocery_or_supermarket|hardware_store",
														"dentist|doctor|gym|health|hospital|pharmacy|physiotherapist|spa",
														"atm|bank|finance",
														"gas_station",
														"university|school|library|book_store",
														"amusement_park|aquarium|art_gallery|bowling_alley|casino|movie_rental|movie_theater|museum|night_club|park|stadium|zoo",
														"beauty_salon|car_dealer|car_rental|car_repair|car_wash|florist|laundry|place_of_worship|storage"};
	public static Map<String, String> catType = new HashMap<String, String>();

	private void populateMarkerMap() {
		int i = 0;
		for (String cat : categories) {
			catType.put(cat, gType[i]);
			i++;
		}
	}
	
	public PlacesUtil(){
		populateMarkerMap();
	}

	public PlacesList getNearPlaces(Todo todo) {

		PlacesList res = new PlacesList();
		try {
			// HttpGet get = new HttpGet(PLACES_SEARCH_URL);
			HttpClient httpclient = new DefaultHttpClient();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("key", API_KEY));
			params.add(new BasicNameValuePair("location", String
					.valueOf(HomeScreenActivity.currentLatitude)
					+ ","
					+ String.valueOf(HomeScreenActivity.currentLongitude)));
			params.add(new BasicNameValuePair("radius", _radius));
			System.out.println("Category : "+catType.get(todo.getCategory()));
			if(todo.getPrefLoc().equals("All")){
				params.add(new BasicNameValuePair("types", catType.get(todo.getCategory())));
			}
			else {
				params.add(new BasicNameValuePair("types", todo.getPrefLoc()));
			}
			
			params.add(new BasicNameValuePair("sensor", "false"));
			//params.add(new BasicNameValuePair("opennow", "true"));
			params.add(new BasicNameValuePair("keyword", todo.getNote()));

			URI uri = new URI(PLACES_SEARCH_URL + "?"
					+ URLEncodedUtils.format(params, "utf-8"));

			HttpUriRequest request = new HttpGet(uri);

			request.setHeader("Accept",
					"application/json, text/javascript, */*; q=0.01");

			HttpResponse response = null;

			try {
				response = httpclient.execute(request);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			}

			String data = EntityUtils.toString(response.getEntity());
			Gson g = new Gson();
			res = g.fromJson(data, PlacesList.class);
			// nearMe = res;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

}
