package com.slide531.googleplacesautocomplete;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import android.location.Location;
import android.util.Log;

import com.google.gson.Gson;
import com.slide531.gson.googleplacesautocomplete.PlaceDetails;
import com.slide531.gson.googleplacesautocomplete.PlacesAutoCompletePredictions;

public class GooglePlacesApiWrapper {

	private static final String LOG_TAG = "GooglePlacesAutocomplete";

	private static final String googlePlacesAutoCompleteUrlFormat = "https://maps.googleapis.com/maps/api/place/autocomplete/json?sensor=true&language=en&key=%1$s&input=%2$s";
	private static final String googlePlacesDetailsUrlFormat = "https://maps.googleapis.com/maps/api/place/details/json?sensor=true&key=%1$s&reference=%2$s";
	private static final String googlePlacesBiasFormat = "&location=%1$f,%2$f&radius=%3$d";

	private static final String API_KEY = "your api key here";

	public static ArrayList<PlacesAutoCompletePredictions.PlacesAutoCompletePrediction> autocomplete(
			String input, Location locationBias, Long radiusBias) {

		String url = buildAutoCompleteURL(input, locationBias, radiusBias);
		String jsonResults = getGooglePlacesResults(url);
		ArrayList<PlacesAutoCompletePredictions.PlacesAutoCompletePrediction> resultList = parseAutoCompleteJsonResults(jsonResults);

		return resultList;
	}

	public static PlaceDetails getPlaceDetails(String ref) {
		String url = buildPlaceDetailsUrl(ref);
		String jsonResults = getGooglePlacesResults(url);
		PlaceDetails details = parsePlaceDetailsJsonResults(jsonResults);
		return details;
	}

	private static PlaceDetails parsePlaceDetailsJsonResults(String jsonResults) {
		Gson gson = new Gson();
		PlaceDetails details = gson.fromJson(jsonResults, PlaceDetails.class);
		return details;
	}

	private static ArrayList<PlacesAutoCompletePredictions.PlacesAutoCompletePrediction> parseAutoCompleteJsonResults(
			String jsonResults) {
		ArrayList<PlacesAutoCompletePredictions.PlacesAutoCompletePrediction> resultList = null;
		try { 
			Gson gson = new Gson();
			PlacesAutoCompletePredictions predictionsObject = gson.fromJson(
					jsonResults, PlacesAutoCompletePredictions.class);
			resultList = new ArrayList<PlacesAutoCompletePredictions.PlacesAutoCompletePrediction>(
					Arrays.asList(predictionsObject.predictions));
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		return resultList;
	}

	private static String getGooglePlacesResults(String urlString) {
		HttpURLConnection conn = null;
		StringBuilder jsonResults = new StringBuilder();
		try {
			URL url = new URL(urlString);
			conn = (HttpURLConnection) url.openConnection();
			InputStreamReader in = new InputStreamReader(conn.getInputStream());

			// Load the results into a StringBuilder
			int read;
			char[] buff = new char[1024];
			while ((read = in.read(buff)) != -1) {
				jsonResults.append(buff, 0, read);
			}
		} catch (MalformedURLException e) {
			Log.e(LOG_TAG, "Error processing Places API URL", e);
		} catch (IOException e) {
			Log.e(LOG_TAG, "Error connecting to Places API", e);
		} catch (Exception e) {
			Log.e(LOG_TAG, e.getMessage());
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return jsonResults.toString();
	}

	private static String buildAutoCompleteURL(String input,
			Location locationBias, Long radiusBias) {
		StringBuilder sb = new StringBuilder();
		try {
			String encodedInput = URLEncoder.encode(input, "utf8");
			sb.append(String.format(googlePlacesAutoCompleteUrlFormat, API_KEY,
					encodedInput));
			if ( locationBias != null ) {
				sb.append( getBiasString(locationBias, radiusBias));
			}
			Log.i(LOG_TAG, sb.toString());

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	private static String getBiasString(Location locationBias, Long radiusBias) {
		String val = "";
		if (locationBias != null && radiusBias != null) {
			double lat = locationBias.getLatitude();
			double lon = locationBias.getLongitude();
			if (lat >= -90 && lat <= 90 && lon >= -180 && lon <= 180
					&& radiusBias > 0 && radiusBias < 50000) {
				val = String.format(Locale.US, googlePlacesBiasFormat, lat, lon,
						radiusBias);
			}
		}
		return val;
	}

	private static String buildPlaceDetailsUrl(String reference) {
		String val = null;
		try {
			String encodedReference = URLEncoder.encode(reference, "utf8");
			val = String.format(googlePlacesDetailsUrlFormat, API_KEY,
					encodedReference);
			Log.i(LOG_TAG, val);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return val;
	}

}
