package com.stalker.places;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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

import com.stalker.places.PlacesList;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.gson.Gson;

public class PlacesUtil {
	
	public static String types = "food";
	
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	
	// Google API Key
    private static final String API_KEY = "AIzaSyDZK2TYRjLqK-lOX-ALkUJd8276FUEPPxw";
 
    // Google Places serach url's
    private static final String PLACES_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";
    private static final String PLACES_TEXT_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/search/json?";
    private static final String PLACES_DETAILS_URL = "https://maps.googleapis.com/maps/api/place/details/json?";
    
    public static double _latitude = 37.3357190;
    public static double _longitude = -121.8867080;
    private String _radius = "500";
    public static PlacesList nearMe;
	
	public PlacesList getNearPlaces() {
		PlacesList res = new PlacesList();
				try{
				    //HttpGet get = new HttpGet(PLACES_SEARCH_URL);
				    HttpClient httpclient = new DefaultHttpClient();
				    List<NameValuePair> params = new ArrayList<NameValuePair>();
				    params.add(new BasicNameValuePair("key", API_KEY));
				    params.add(new BasicNameValuePair("location", _latitude + "," + _longitude));
				    params.add(new BasicNameValuePair("radius", _radius));
				    params.add(new BasicNameValuePair("types", types));
				    params.add(new BasicNameValuePair("sensor", "false"));
				    
				    
				    URI uri = new URI( PLACES_SEARCH_URL + "?" + URLEncodedUtils.format( params, "utf-8" ));

				    HttpUriRequest request = new HttpGet(uri);

				    request.setHeader("Accept", "application/json, text/javascript, */*; q=0.01");

				    HttpResponse response = null;

				    try {
				        response = httpclient.execute(request);
				    } catch (ClientProtocolException e) {
				        e.printStackTrace();
				    }
				    
				    String data = EntityUtils.toString(response.getEntity());
				    Gson g = new Gson();
				    res = g.fromJson(data, PlacesList.class);
				    nearMe = res;
				    
				} catch (Exception e) {
				    e.printStackTrace();
				}
				return res;
			}

}
