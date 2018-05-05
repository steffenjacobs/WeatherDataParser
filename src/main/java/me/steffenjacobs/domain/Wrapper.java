package me.steffenjacobs.domain;

import java.util.Map;

public class Wrapper {

	private CsvEntry entry;
	private Map<String, AverageList> map;

	public Wrapper(CsvEntry entry, Map<String, AverageList> map) {
		super();
		this.entry = entry;
		this.map = map;
	}

	public CsvEntry getLine() {
		return entry;
	}

	public void setLine(CsvEntry line) {
		this.entry = line;
	}

	public Map<String, AverageList> getMap() {
		return map;
	}

	public void setMap(Map<String, AverageList> map) {
		this.map = map;
	}

}
