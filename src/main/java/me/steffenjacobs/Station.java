package me.steffenjacobs;

/**
 * Domain object of modelling a weather station
 * 
 * @author Steffen Jacobs
 */
public class Station {
	private int stationId;
	private double lat, lng;
	private String name, county;

	public Station(int stationId, double lat, double lng, String name) {
		super();
		this.stationId = stationId;
		this.lat = lat;
		this.lng = lng;
		this.name = name;
	}

	public Station() {

	}

	public int getStationId() {
		return stationId;
	}

	public void setStationId(int stationId) {
		this.stationId = stationId;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Station [stationId=");
		builder.append(stationId);
		builder.append(", lat=");
		builder.append(lat);
		builder.append(", lng=");
		builder.append(lng);
		builder.append(", name=");
		builder.append(name);
		builder.append(", county=");
		builder.append(county);
		builder.append("]");
		return builder.toString();
	}
}
