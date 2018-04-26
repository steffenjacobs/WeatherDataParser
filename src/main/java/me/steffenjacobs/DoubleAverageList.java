package me.steffenjacobs;

import java.util.ArrayList;
import java.util.List;

public class DoubleAverageList implements AverageList {

    List<Double> values = new ArrayList<>();

    @Override
    public void addValue(Object value) {
        values.add((Double) value);
    }

    @Override
    public Double getAverage() {
        return values.stream().reduce(0d, (a, b) -> a + b) / values.size();
    }

}
