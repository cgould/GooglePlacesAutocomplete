package com.slide531.googleplacesautocomplete;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.slide531.gson.googleplacesautocomplete.PlaceDetails;
import com.slide531.gson.googleplacesautocomplete.PlacesAutoCompletePredictions.PlacesAutoCompletePrediction;

public class MainActivity extends Activity implements OnItemClickListener {

	private PlacesAutoCompleteAdapter autoCompleteAdapter;
	private TextView placeDetails;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initAutoComplete();
	}

	private void initAutoComplete() {
		Location lastKnownLocation = getLocationForLocationBias();
		
		AutoCompleteTextView autoCompView = (AutoCompleteTextView) findViewById(R.id.autocomplete);
		placeDetails = (TextView)findViewById(R.id.place_details);
		autoCompleteAdapter = new PlacesAutoCompleteAdapter(this,
				R.layout.list_item, lastKnownLocation, 5000);
		autoCompView.setAdapter(autoCompleteAdapter);
		autoCompView.setOnItemClickListener(this);
	}

	private Location getLocationForLocationBias() {
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE); 
		Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		return lastKnownLocation;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	@Override
	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long id) {
		PlacesAutoCompletePrediction prediction = (PlacesAutoCompletePrediction) adapterView.getItemAtPosition(position);
		String reference = prediction.reference;
		GetPlaceDetails gpd = new GetPlaceDetails();
		gpd.execute(reference);
	}
	
	private class GetPlaceDetails extends AsyncTask<String, Void, PlaceDetails> {

		@Override
		protected void onPostExecute(PlaceDetails result) {
			placeDetails.setText(result.toString());
		}

		@Override
		protected PlaceDetails doInBackground(String... params) {
			PlaceDetails details = GooglePlacesApiWrapper.getPlaceDetails(params[0]);
			return details;
		}
		
	}

}
