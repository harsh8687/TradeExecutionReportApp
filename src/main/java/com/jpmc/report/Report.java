package com.jpmc.report;

import java.util.HashMap;
import java.util.Map;

public class Report<T, U> {

    private Map<T, U> data;

    public Report(Map<T, U> data) {
        this.data = data;
    }

    public Map<T, U> getData() {
        return new HashMap<>(data);
    }

    public void displayReport() {
        data.forEach((k, v) -> System.out.println("Key : " + k + " Value : " + v));
    }
}
