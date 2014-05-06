package com.stalker;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import com.stalker.places.Place;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

@SuppressLint("ValidFragment")
public class ShowPlaceDialog extends DialogFragment {
	TextView priceLevel = null;
	TextView rating = null;
	TextView mTVVicinity = null;
	Place mPlace = null;
	DisplayMetrics mMetrics = null;
	Bitmap bmp = null;

	public ShowPlaceDialog() {
		super();
	}

	public ShowPlaceDialog(Place place, DisplayMetrics dm) {
		super();
		this.mPlace = place;
		this.mMetrics = dm;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		// For retaining the fragment on screen rotation
		setRetainInstance(true);
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.dialog_layout, null);

		// Getting reference to TextView to display place vicinity
		mTVVicinity = (TextView) v.findViewById(R.id.tv_vicinity);
		priceLevel = (TextView) v.findViewById(R.id.price_level);
		rating = (TextView) v.findViewById(R.id.rating);

		if (mPlace != null) {

			// Setting the title for the Dialog Fragment
			getDialog().setTitle(mPlace.name);

			// Setting the vicinity of the place
			mTVVicinity.setText("Address : " + mPlace.vicinity);

			// Creating an array of ImageDownloadTask to download photos

			if (mPlace.rating != null)
				rating.setText("Rating : " + mPlace.rating);
			if (mPlace.price_level != null)
				priceLevel.setText("Price Range : " + mPlace.price_level);

		}
		return v;
	}

	@Override
	public void onDestroyView() {
		if (getDialog() != null && getRetainInstance())
			getDialog().setDismissMessage(null);
		super.onDestroyView();
	}

	private Bitmap downloadImage(String strUrl) throws IOException {
		Bitmap bitmap = null;
		InputStream iStream = null;
		try {
			URL url = new URL(strUrl);

			/** Creating an http connection to communcate with url */
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();

			/** Connecting to url */
			urlConnection.connect();

			/** Reading data from url */
			iStream = urlConnection.getInputStream();

			/** Creating a bitmap from the stream returned from the url */
			bitmap = BitmapFactory.decodeStream(iStream);

		} catch (Exception e) {
			Log.d("Exception while downloading url", e.toString());
		} finally {
			iStream.close();
		}
		return bitmap;
	}

	// private class ImageDownloadTask extends AsyncTask<String, Integer,
	// Bitmap>{
	// Bitmap bitmap = null;
	// private WeakReference<ShowPlaceDialog> fragmentWeakRef;
	//
	// public ImageDownloadTask(ShowPlaceDialog fragment) {
	// // TODO Auto-generated constructor stub
	// fragmentWeakRef = new WeakReference<ShowPlaceDialog>(fragment);
	// }
	//
	// @Override
	// protected Bitmap doInBackground(String... url) {
	// try{
	// // Starting image download
	// bitmap = downloadImage(url[0]);
	// }catch(Exception e){
	// Log.d("Background Task",e.toString());
	// }
	// return bitmap;
	// }
	//
	// @Override
	// protected void onPostExecute(Bitmap result) {
	// super.onPostExecute(result);
	//
	// // Creating an instance of ImageView to display the downloaded image
	// ImageView iView = new ImageView(getActivity().getBaseContext());
	//
	// // Setting the downloaded image in ImageView
	// iView.setImageBitmap(result);
	//
	// // Adding the ImageView to ViewFlipper
	//
	// //mFlipper.setImageBitmap(result);
	//
	// // Showing download completion message
	// Toast.makeText(getActivity().getBaseContext(),
	// "Image downloaded successfully", Toast.LENGTH_SHORT).show();
	// }
	// }
}
