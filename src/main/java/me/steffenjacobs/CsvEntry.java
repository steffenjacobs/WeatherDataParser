package me.steffenjacobs;

import static me.steffenjacobs.WeatherRetriever.NUMBER_FORMAT;

public class CsvEntry {

    private int coopId;
    private int year;
    private int month;
    private int day;
    private double precipitation;
    private boolean isPrecipitation;
    private int maxTemp;
    private int minTemp;
    private double meanTemp;
    private double meanTempCelsius;
    private String county;

    public CsvEntry(Station station, String cellEntry) {
        String[] split = cellEntry.split(",");
        coopId = Integer.parseInt(split[0]);
        year = Integer.parseInt(split[1]);
        month = Integer.parseInt(split[2]);
        day = Integer.parseInt(split[3]);
        precipitation = Double.parseDouble(split[4]);
        isPrecipitation = precipitation > 0 ? true : false;
        maxTemp = (int) Double.parseDouble(split[5]);
        minTemp = (int) Double.parseDouble(split[6]);
        meanTemp = Double.parseDouble(NUMBER_FORMAT.format(split[7].trim().equals("") ?
                (Double.parseDouble(split[5]) + Double.parseDouble(split[6])) / 2 :
                Double.parseDouble(split[7].trim())));
        meanTempCelsius = Double.parseDouble(NUMBER_FORMAT.format(meanTemp == -99.90000 ? meanTemp :
                ((meanTemp - 32) * 5) / 9));
        county = station.getCounty();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(coopId).append(",");
        stringBuilder.append(year).append(",");
        stringBuilder.append(month).append(",");
        stringBuilder.append(day).append(",");
        stringBuilder.append(year + "-" + month + "-" + day).append(",");
        stringBuilder.append(precipitation).append(",");
        stringBuilder.append(isPrecipitation).append(",");
        stringBuilder.append(maxTemp).append(",");
        stringBuilder.append(minTemp).append(",");
        stringBuilder.append(meanTemp).append(",");
        stringBuilder.append(meanTempCelsius).append(",");
        stringBuilder.append(county).append("\n");
        return stringBuilder.toString();
    }

}
