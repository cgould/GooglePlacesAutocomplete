package com.slide531.gson.googleplacesautocomplete;

public class PlaceDetails {

	public String [] html_attributions;
	public Result result;
	public String status;
	
	@Override
	public String toString() {
		return result.toString();
	}

	public static class Result {
		public AddressComponent [] address_components;
		public Event [] events;
		public String formatted_address;
		public String formatted_phone_number;
		public Geometry geometry;
		public String icon;
		public String id;
		public String international_phone_number; 
		public String name; 
		public String rating;
		public String reference;
		public Review [] reviews;
		public String [] types;
		public String url;
		public String vicinity;
		public String website;
		
		public static class AddressComponent {
			public String long_name;
			public String short_name;
			public String [] types;
		}
		
		public static class Event {
			public String event_id;
			public long start_time;
			public String summary;
			public String url;
		}
		
		public static class Geometry {
			public Location location;
		}
		
		public static class Review {
			public Aspect [] aspects;
			public String author_name;
			public String author_url;
			public String text;
			public long time;
		}
		
		public static class Location {
			public Double lat;
			public Double lng;
		}
		
		public static class Aspect {
			public int rating;
			public String type;
		}
		
		@Override
		public String toString() {
			return name + "\n" + formatted_address + "\n(" + geometry.location.lat.toString() +"," + this.geometry.location.lng.toString() + ")";
		}

	}
}
