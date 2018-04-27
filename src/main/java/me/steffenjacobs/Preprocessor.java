package me.steffenjacobs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class Preprocessor {

    private static String PRECIPITATION = "precipitation";
    private static String MAX_TEMPERATURE = "maxTemp";
    private static String MIN_TEMPERATURE = "minTemp";
    private static String MEAN_TEMPERATURE = "meanTemp";

    public static void processExcelFile() {
        Map<CsvEntry, Wrapper> entriesToValues = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(new File("weatherData.csv")))) {
            String line;
            while ((line = br.readLine()) != null) {
                if(line.startsWith("COOPID")){
                    continue;
                }
                createCsvEntry(entriesToValues, line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        createNewExcelFile(entriesToValues);
    }

    private static void createCsvEntry(Map<CsvEntry, Wrapper> entriesToValues, String line) {
        boolean isEqual = false;
        CsvEntry csvEntry = new CsvEntry(line);
        CsvEntry other = null;
//        System.out.println(entriesToValues.containsKey(csvEntry));
        if (entriesToValues.containsKey(csvEntry)) {
//        	System.out.println("contin");
        	isEqual = true;
        	other = entriesToValues.get(csvEntry).getLine();
        }
//        for (CsvEntry entry : entriesToValues.keySet()) {
//            isEqual = csvEntry.equals(entry);
//            if (isEqual) {
//                other = entry;
//                break;
//            }
//        }
        if (isEqual) {
            addCsvEntryToMap(entriesToValues, csvEntry, other);
        } else {
            createMapForCsvEntry(entriesToValues, csvEntry);
        }
    }

    private static void addCsvEntryToMap(Map<CsvEntry, Wrapper> entriesToValues, CsvEntry csvEntry,
                                         CsvEntry other) {
        Map values = entriesToValues.get(other).getMap();
        DoubleAverageList precipitationList = (DoubleAverageList) values.get(PRECIPITATION);
        precipitationList.addValue(csvEntry.getPrecipitation());
        IntegerAverageList maxTempList = (IntegerAverageList) values.get(MAX_TEMPERATURE);
        maxTempList.addValue(csvEntry.getMaxTemp());
        IntegerAverageList minTempList = (IntegerAverageList) values.get(MIN_TEMPERATURE);
        minTempList.addValue(csvEntry.getMinTemp());
        DoubleAverageList meanTempList = (DoubleAverageList) values.get(MEAN_TEMPERATURE);
        meanTempList.addValue(csvEntry.getMeanTemp());
    }

    private static void createMapForCsvEntry(Map<CsvEntry, Wrapper> entriesToValues, CsvEntry
            csvEntry) {
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

    private static void createNewExcelFile(Map<CsvEntry, Wrapper> entriesToValues) {
        try (PrintWriter pw = new PrintWriter(new FileWriter("weatherData_preprocessed.csv"))) {
            pw.write("COOPID,YEAR,MONTH,DAY,DATE,PRECIPITATION,IsPrecipitation,MAX TEMP,MIN TEMP, MEAN TEMP,MEAN TEMP" +
                    " (Celsius),COUNTY,WEEKDAY\n");
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
        processExcelFile();
    }
}
