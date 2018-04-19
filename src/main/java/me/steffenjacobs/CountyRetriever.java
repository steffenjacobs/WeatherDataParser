package me.steffenjacobs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** @author Steffen Jacobs */
public class CountyRetriever {

	private static final Logger log = Logger.getLogger("main-logger");
	
	private static final String WEATHER_URL = "https://climatecenter.fsu.edu/climate-data-access-tools/downloadable-data";
	private static final String AREA_URL = "https://geo.fcc.gov/api/census/area?lat=%s&lon=%s";

	private static final Pattern PAT_LONGLAT = Pattern.compile(
			"var latlng[0-9]* = new google\\.maps\\.LatLng\\(([0-9]+\\.[0-9]+), ([-]?[0-9]+\\.[0-9]+)\\);\\R*\\s+var marker[0-9]* = new google\\.maps\\.Marker\\(\\{\\R*\\s+position: latlng[0-9]*,\\R*\\s+title:\"([\\w\\s*]+)\"");
	private static final Pattern PAT_JSON_COUNTY = Pattern.compile("\"county_name\":\"([\\w\\.?[-]?\\s*]+)\",");


	public static void main(String[] args) throws Exception {
		String htmlString = sendGet(WEATHER_URL);
		Matcher matcher = PAT_LONGLAT.matcher(htmlString);
		List<Station> stations = new ArrayList<>();
		while (matcher.find()) {
			Station station = new Station(Double.parseDouble(matcher.group(1)), Double.parseDouble(matcher.group(2)), matcher.group(3));
			stations.add(station);
		}

		for (Station station : stations) {
			String html = sendGet(String.format(AREA_URL, station.getLat(), station.getLng()));
			Matcher matcher2 = PAT_JSON_COUNTY.matcher(html);
			if (!matcher2.find()) {
				log.log(Level.SEVERE, "Error: County for station {0} not found.", station.getName());
			} else {
				station.setCounty(matcher2.group(1));
			}

			System.out.println(String.format("%s: Lat: %s, Lng: %s, County: %s", station.getName(), station.getLat(), station.getLng(), station.getCounty()));
		}

	}

	private static String sendGet(String url) throws MalformedURLException, IOException {

		HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		return response.toString();
	}

}
