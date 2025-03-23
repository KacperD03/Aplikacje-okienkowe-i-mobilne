package com.example.demo;

public class Group {
    private String name;
    private int capacity;

    public Group() {
    }

    public Group(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
    }

    // Gettery i settery
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}