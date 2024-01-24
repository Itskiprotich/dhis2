package com.nacare.capture.data.model;

import java.util.ArrayList;
import java.util.List;

public class FormatterClass {
    public List<HomeData> generateHomeData() {
        // Replace this logic with your actual implementation
        List<HomeData> homeDataList = new ArrayList<>();
        // Example: Adding dummy data
        homeDataList.add(new HomeData("70", "Number of notifications made by facility"));
        homeDataList.add(new HomeData("310", "Number of active notifications made by facility (not closed within 60 days)"));

        return homeDataList;
    }
}
