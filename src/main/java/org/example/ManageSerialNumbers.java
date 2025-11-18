package org.example;

import java.util.List;
import java.util.ArrayList;

public class ManageSerialNumbers {

    private static final List<Long> stationSerialNumbers = new ArrayList<>();

    public static List<Long> getStationSerialNumbers() {
        return stationSerialNumbers;
    }

    public static void removeSerialNumber(Long serialNumber){
        stationSerialNumbers.remove(serialNumber);
    }

    // Simplest way to add to the tracking list
    public static void recordSerialNumber(Long serialNumber){
        stationSerialNumbers.add(serialNumber);
    }

    // Core uniqueness check
    public static boolean isSerialNumberUnique(Long serialNumber) {
        return !stationSerialNumbers.contains(serialNumber);
    }

    // Add the reset for testing
    public static void reset() {
        stationSerialNumbers.clear();
    }
}