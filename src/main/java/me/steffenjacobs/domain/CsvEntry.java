package me.steffenjacobs.domain;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static me.steffenjacobs.WeatherRetriever.NUMBER_FORMAT;

public class CsvEntry {

    private int coopId;
    private int year;
    private int month;
    private int day;
    private double precipitation;
    private int maxTemp;
    private int minTemp;
    private double meanTemp;
    private String county;
    private String weekday;

    public CsvEntry(String line) {
        String[] split = line.split(",");
        coopId = Integer.parseInt(split[0]);
        year = Integer.parseInt(split[1]);
        month = Integer.parseInt(split[2]);
        day = Integer.parseInt(split[3]);
        precipitation = Double.parseDouble(split[5]);
        maxTemp = (int) Double.parseDouble(split[7]);
        minTemp = (int) Double.parseDouble(split[8]);
        meanTemp = Double.parseDouble(split[9]);
        county = split[11];
        setWeekday();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(coopId).append(",");
        stringBuilder.append(year).append(",");
        stringBuilder.append(month).append(",");
        stringBuilder.append(day).append(",");
        stringBuilder.append(this.getDateString()).append(",");
        stringBuilder.append(precipitation).append(",");
        stringBuilder.append(precipitation > 0 ? true : precipitation < 0 ? "null" : false).append(",");
        stringBuilder.append(maxTemp).append(",");
        stringBuilder.append(minTemp).append(",");
        stringBuilder.append(meanTemp).append(",");
        stringBuilder.append(Double.parseDouble(NUMBER_FORMAT.format(meanTemp == -99.90000 ? meanTemp :
                ((meanTemp - 32) * 5) / 9))).append(",");
        stringBuilder.append(county).append(",");
        stringBuilder.append(weekday).append("\n");
        return stringBuilder.toString();
    }

    
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((county == null) ? 0 : county.hashCode());
		result = prime * result + day;
		result = prime * result + month;
		result = prime * result + year;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CsvEntry other = (CsvEntry) obj;
		if (county == null) {
			if (other.county != null)
				return false;
		} else if (!county.equals(other.county))
			return false;
		if (day != other.day)
			return false;
		if (month != other.month)
			return false;
		if (year != other.year)
			return false;
		return true;
	}
    
    
    
    
//    public boolean equals(CsvEntry other) {
//    	System.out.println("equl");
//        if (this.county == null || other.county == null) {
//            return false;
//        }
//        return this.county.equals(other.getCounty()) && this.getDateString().equals(other.getDateString());
//    }

    public double getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(double precipitation) {
        this.precipitation = precipitation;
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(int maxTemp) {
        this.maxTemp = maxTemp;
    }

    public int getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(int minTemp) {
        this.minTemp = minTemp;
    }

    public double getMeanTemp() {
        return meanTemp;
    }

    public void setMeanTemp(double meanTemp) {
        this.meanTemp = meanTemp;
    }

    public String getCounty() {
        return county;
    }

    private String getDateString() {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            LocalDate localDate = LocalDate.of(this.year, this.month, this.day);
            return formatter.format(localDate);
        } catch(DateTimeException e) {
            return "INVALID";
        }
    }

    private void setWeekday() {
        try {
            LocalDate localDate = LocalDate.of(this.year, this.month, this.day);
            java.time.DayOfWeek dayOfWeek = localDate.getDayOfWeek();
            weekday = dayOfWeek.toString();
        } catch(DateTimeException e) {
            weekday = "";
        }
    }

}
