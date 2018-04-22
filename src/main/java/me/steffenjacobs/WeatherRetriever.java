package me.steffenjacobs;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class WeatherRetriever {

	public static void post() {

		try (PrintWriter pw = new PrintWriter(new FileWriter("testFile"));) {
			Map<Integer, Station> countyForStation = CountyRetriever.getCountyForStation();

			for (int stationId : countyForStation.keySet()) {
				URL url = new URL("https://climatecenter.fsu.edu/jumi/climate_visualization/Climate_Data.php");
				URLConnection con = url.openConnection();
				HttpURLConnection http = (HttpURLConnection) con;
				http.setRequestMethod("POST");
				http.setDoOutput(true);
				Map<String, String> arguments = new HashMap<>();
				arguments.put("down_day", "1");
				arguments.put("down_day_end", "31");
				arguments.put("down_station", String.valueOf(stationId));
				arguments.put("down_Submit", "Download+File");
				arguments.put("down_time", "1");
				arguments.put("down_time_end", "10");
				arguments.put("down_variable", "all");
				arguments.put("down_year", "2010");
				arguments.put("down_year_end", "2016");
				StringJoiner sj = new StringJoiner("&");
				for (Map.Entry<String, String> entry : arguments.entrySet())
					try {
						sj.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "="
								+ URLEncoder.encode(entry.getValue(), "UTF-8"));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				byte[] out = sj.toString().getBytes(StandardCharsets.UTF_8);
				int length = out.length;
				http.setFixedLengthStreamingMode(length);
				http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
				try (OutputStream os = http.getOutputStream()) {

					http.connect();
					os.write(out);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try (BufferedReader is = new BufferedReader(new InputStreamReader(http.getInputStream()))) {
					System.out.println("test");
					String line = "";
					while ((line = is.readLine()) != null) {
						if (/* line.endsWith(", ") */line.matches(".*, ")) {
							System.out.println(line + countyForStation.get(stationId).getCounty() + ",");
							pw.write(line + countyForStation.get(stationId).getCounty() + ",\n	");
						} else {
							System.out.println(line + "," + countyForStation.get(stationId).getCounty() + ",");
							pw.write(line + "," + countyForStation.get(stationId).getCounty() + ",\n");
						}
					}
					http.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (ProtocolException e) {
			e.printStackTrace();
		} // PUT is another valid option
		catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public static void main(String[] args) {
		post();
	}
}
