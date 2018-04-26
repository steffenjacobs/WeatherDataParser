package me.steffenjacobs;

import java.util.ArrayList;
import java.util.List;

public class IntegerAverageList implements AverageList {

    private List<Integer> values = new ArrayList<>();

    @Override
    public void addValue(Object value) {
        values.add((Integer) value);
    }

    @Override
    public Integer getAverage() {
        return values.stream().reduce(0, (a, b) -> a + b) / values.size();
    }

}
