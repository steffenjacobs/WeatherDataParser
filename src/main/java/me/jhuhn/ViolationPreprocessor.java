package me.jhuhn;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import org.apache.commons.csv.CSVPrinter;

public class ViolationPreprocessor {

	private static final Logger LOG = Logger.getLogger(ViolationPreprocessor.class.getSimpleName());

	public static void main(String[] args) throws IOException {

		CSVParser csvFileParser = CSVFormat.DEFAULT.parse(new FileReader(new File("data_set_original.csv")));
		BufferedWriter writer = Files.newBufferedWriter(Paths.get("data_set_modified_violations.csv"));
		CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
		long now = System.currentTimeMillis();
		LOG.info("Started preprocessing of violations");

		int k = 0;
		for (CSVRecord csvRecord : csvFileParser) {
			String id = csvRecord.get(0);
			String state = csvRecord.get(1);
			String stop_date = csvRecord.get(2);
			String stop_time = csvRecord.get(3);
			String location_raw = csvRecord.get(4);
			String county_name = csvRecord.get(5);
			String county_fips = csvRecord.get(6);
			String fine_grained_location = csvRecord.get(7);
			String police_department = csvRecord.get(8);
			String driver_gender = csvRecord.get(9);
			String driver_age_raw = csvRecord.get(10);
			String driver_age = csvRecord.get(11);
			String driver_race_raw = csvRecord.get(12);
			String driver_race = csvRecord.get(13);
			String violation_raw = csvRecord.get(14);

			String violationCSV = csvRecord.get(15);

			String search_conducted = csvRecord.get(16);
			String search_type_raw = csvRecord.get(17);
			String search_type = csvRecord.get(18);
			String contraband_found = csvRecord.get(19);
			String stop_outcome = csvRecord.get(20);
			String is_arrested = csvRecord.get(21);
			String officer_id = csvRecord.get(22);
			String officer_gender = csvRecord.get(23);
			String officer_age = csvRecord.get(24);
			String officer_race = csvRecord.get(25);
			String officer_rankr = csvRecord.get(26);
			String out_of_state = csvRecord.get(27);

			if (violationCSV.trim().equals("")) {
				violationCSV = "NONE";
			}

			String[] splitViolation = violationCSV.split(",");

			for (int i = 0; i < splitViolation.length; i++) {
				// write file
				csvPrinter.printRecord(id, state, stop_date, stop_time, location_raw, county_name, county_fips, fine_grained_location, police_department, driver_gender,
						driver_age_raw, driver_age, driver_race_raw, driver_race, violation_raw, splitViolation[i], search_conducted, search_type_raw, search_type,
						contraband_found, stop_outcome, is_arrested, officer_id, officer_gender, officer_age, officer_race, officer_rankr, out_of_state);
			}
			k++;
		}

		csvPrinter.flush();
		csvPrinter.close();
		LOG.log(Level.INFO, "Finished processing {0} entries ({1}ms).", new Object[] { k, System.currentTimeMillis() - now });
	}
}
