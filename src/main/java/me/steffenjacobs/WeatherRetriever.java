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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.StringJoiner;

public class WeatherRetriever {

	static DecimalFormat NUMBER_FORMAT = (DecimalFormat) NumberFormat.getNumberInstance(Locale.US);

	public static void post() {
		NUMBER_FORMAT.applyPattern("0.00000");
		Locale.setDefault(new Locale("en", "US"));

		try (PrintWriter pw = new PrintWriter(new FileWriter("weatherData.csv"));) {
			Map<Integer, Station> countyForStation = CountyRetriever.getCountyForStation();

			int count = 0;
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
				arguments.put("down_time_end", "12");
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
						if (line.startsWith("COOPID")) {
							if (count == 0) {
								count++;
								pw.write(createHeader(line) + "\n");
							} else {
								continue;
							}
						} else {
							String newLine = createLine(countyForStation.get(stationId), line);
							System.out.print(newLine);
							pw.write(newLine);
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

	private static String createLine(Station station, String line) {
		String[] split = line.split(",");
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < split.length; i++) {
			if (!split[i].trim().equals("")) {
				sb.append(split[i] + ",");
			}

			switch (i) {
				case 3:
					sb.append(split[1] + "-" + split[2] + "-" + split[3] + ",");
					break;
				case 4:
					sb.append((Double.valueOf(split[4]) > 0 ? true : Double.valueOf(split[4]) < 0 ? "null" : false) + ",");
					break;
				case 7:
					try {
						String meanTemp = split[7].trim();
						if (split[7].trim().equals("")) {
							meanTemp = NUMBER_FORMAT.format(((Double.valueOf(split[5]) + Double.valueOf(split[6])) / 2));
							sb.append(meanTemp + ",");
						}
						if (meanTemp.trim().equals("-99.90000")) {
							sb.append("-99.99000,");
						} else {
							sb.append(NUMBER_FORMAT.format(((Double.parseDouble(meanTemp) - 32) * 5) / 9) + ",");
						}
						sb.append(station.getCounty());
					} catch (NumberFormatException e) {
						System.err.println("number missing");
						System.exit(0);
					}
					break;
			}
		}
		sb.append("\n");
		return sb.toString();
	}

	private static String createHeader(String line) {
		String[] header = line.split(",");
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < header.length; i++) {
			sb.append(header[i] + ",");
			switch (i) {
				case 3:
					sb.append(" DATE,");
					break;
				case 4:
					sb.append(" IsPrecipitation,");
					break;
				case 7:
					sb.append(" MEAN TEMP(Celsius),");
					sb.append(" COUNTY");
				default:
					break;
			}
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		post();
		// String a = "34.4,334,";
		// System.out.println(Arrays.toString(a.split(",")));
	}
}