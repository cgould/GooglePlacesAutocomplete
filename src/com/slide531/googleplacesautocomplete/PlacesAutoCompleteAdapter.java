package com.slide531.googleplacesautocomplete;

import java.util.ArrayList;

import android.content.Context;
import android.location.Location;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.slide531.gson.googleplacesautocomplete.PlacesAutoCompletePredictions;
import com.slide531.gson.googleplacesautocomplete.PlacesAutoCompletePredictions.PlacesAutoCompletePrediction;


public class PlacesAutoCompleteAdapter extends ArrayAdapter<PlacesAutoCompletePrediction> implements
		Filterable {
	ArrayList<PlacesAutoCompletePredictions.PlacesAutoCompletePrediction> resultList;

	Location locationBias;
	Long radiusBias;
	
	public PlacesAutoCompleteAdapter(Context context, int textViewResourceId, Location location, long radius) {
		super(context, textViewResourceId);
		locationBias = location;
		radiusBias = radius;
	}

	@Override
	public int getCount() {
		return resultList.size();
	}

	@Override
	public PlacesAutoCompletePrediction getItem(int index) {
		return resultList.get(index);
	}

	@Override
	public Filter getFilter() {
		Filter filter = new Filter() {
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults filterResults = new FilterResults();
				if (constraint != null) {
					// Retrieve the autocomplete results.
					resultList = GooglePlacesApiWrapper.autocomplete(constraint
							.toString(), locationBias, radiusBias);

					// Assign the data to the FilterResults
					filterResults.values = resultList;
					filterResults.count = resultList.size();
				}
				return filterResults;
			}

			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				if (results != null && results.count > 0) {
					notifyDataSetChanged();
				} else {
					notifyDataSetInvalidated();
				}
			}
		};
		return filter;
	}
}
