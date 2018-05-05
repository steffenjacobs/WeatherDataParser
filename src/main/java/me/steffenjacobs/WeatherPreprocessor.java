package me.steffenjacobs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.steffenjacobs.domain.AverageList;
import me.steffenjacobs.domain.CsvEntry;
import me.steffenjacobs.domain.DoubleAverageList;
import me.steffenjacobs.domain.IntegerAverageList;
import me.steffenjacobs.domain.Wrapper;

public class WeatherPreprocessor {

	private static final Logger LOG = Logger.getLogger(WeatherPreprocessor.class.getSimpleName());

	private static final String PRECIPITATION = "precipitation";
	private static final String MAX_TEMPERATURE = "maxTemp";
	private static final String MIN_TEMPERATURE = "minTemp";
	private static final String MEAN_TEMPERATURE = "meanTemp";

	private static final String SOURCE_FILE = "weatherData.csv";
	private static final String TARGET_FILE = "weatherData_preprocessed.csv";

	public static void processCsvFile() {
		Map<CsvEntry, Wrapper> entriesToValues = new HashMap<>();
		try (BufferedReader br = new BufferedReader(new FileReader(new File(SOURCE_FILE)))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("COOPID")) {
					continue;
				}
				createCsvEntry(entriesToValues, line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		saveToCsvFile(entriesToValues);
	}

	private static void createCsvEntry(Map<CsvEntry, Wrapper> entriesToValues, String line) {
		boolean isEqual = false;
		CsvEntry csvEntry = new CsvEntry(line);
		CsvEntry other = null;
		if (entriesToValues.containsKey(csvEntry)) {
			isEqual = true;
			other = entriesToValues.get(csvEntry).getLine();
		}
		if (isEqual) {
			addCsvEntryToMap(entriesToValues, csvEntry, other);
		} else {
			createMapForCsvEntry(entriesToValues, csvEntry);
		}
	}

	private static void addCsvEntryToMap(Map<CsvEntry, Wrapper> entriesToValues, CsvEntry csvEntry, CsvEntry other) {
		Map<String, AverageList> values = entriesToValues.get(other).getMap();
		DoubleAverageList precipitationList = (DoubleAverageList) values.get(PRECIPITATION);
		precipitationList.addValue(csvEntry.getPrecipitation());
		IntegerAverageList maxTempList = (IntegerAverageList) values.get(MAX_TEMPERATURE);
		maxTempList.addValue(csvEntry.getMaxTemp());
		IntegerAverageList minTempList = (IntegerAverageList) values.get(MIN_TEMPERATURE);
		minTempList.addValue(csvEntry.getMinTemp());
		DoubleAverageList meanTempList = (DoubleAverageList) values.get(MEAN_TEMPERATURE);
		meanTempList.addValue(csvEntry.getMeanTemp());
	}

	private static void createMapForCsvEntry(Map<CsvEntry, Wrapper> entriesToValues, CsvEntry csvEntry) {
		Map<String, AverageList> values = new HashMap<>();
		DoubleAverageList precipitationList = new DoubleAverageList();
		precipitationList.addValue(csvEntry.getPrecipitation());
		values.put(PRECIPITATION, precipitationList);
		IntegerAverageList maxTempList = new IntegerAverageList();
		maxTempList.addValue(csvEntry.getMaxTemp());
		values.put(MAX_TEMPERATURE, maxTempList);
		IntegerAverageList minTempList = new IntegerAverageList();
		minTempList.addValue(csvEntry.getMinTemp());
		values.put(MIN_TEMPERATURE, minTempList);
		DoubleAverageList meanTempList = new DoubleAverageList();
		meanTempList.addValue(csvEntry.getMeanTemp());
		values.put(MEAN_TEMPERATURE, meanTempList);

		entriesToValues.put(csvEntry, new Wrapper(csvEntry, values));
	}

	private static void saveToCsvFile(Map<CsvEntry, Wrapper> entriesToValues) {
		try (PrintWriter pw = new PrintWriter(new FileWriter(TARGET_FILE))) {
			pw.write("COOPID,YEAR,MONTH,DAY,DATE,PRECIPITATION,IsPrecipitation,MAX TEMP,MIN TEMP, MEAN TEMP,MEAN TEMP" + " (Celsius),COUNTY,WEEKDAY\n");
			for (Map.Entry<CsvEntry, Wrapper> entry : entriesToValues.entrySet()) {
				CsvEntry csvEntry = entry.getKey();
				Map<String, AverageList> values = entry.getValue().getMap();
				DoubleAverageList precipitationList = (DoubleAverageList) values.get(PRECIPITATION);
				csvEntry.setPrecipitation(precipitationList.getAverage());
				IntegerAverageList maxTempList = (IntegerAverageList) values.get(MAX_TEMPERATURE);
				csvEntry.setMaxTemp(maxTempList.getAverage());
				IntegerAverageList minTempList = (IntegerAverageList) values.get(MIN_TEMPERATURE);
				csvEntry.setMinTemp(minTempList.getAverage());
				DoubleAverageList meanTempList = (DoubleAverageList) values.get(MEAN_TEMPERATURE);
				csvEntry.setMeanTemp(meanTempList.getAverage());
				pw.write(csvEntry.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		LOG.info("Started preprocessing weather data");
		long now = System.currentTimeMillis();
		processCsvFile();
		LOG.log(Level.INFO, "Finished preprocessing weather data ({0}ms)", System.currentTimeMillis() - now);
	}
}
