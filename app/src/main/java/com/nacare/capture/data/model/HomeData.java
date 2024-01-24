package com.nacare.capture.data.model;

public class HomeData {
    private String id;
    private String name;

    public HomeData(String id, String name) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
