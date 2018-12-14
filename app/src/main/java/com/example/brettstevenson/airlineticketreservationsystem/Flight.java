package com.example.brettstevenson.airlineticketreservationsystem;

import java.util.HashMap;
import java.util.Map;

public class Flight {

    private String id;
    private String departure;
    private String arrival;
    private String time;
    private double price;
    private int capacity;
    private int seats;

    public Flight(String id, String departure, String arrival, String time, double price, int capacity, int seats) {
        this.id = id;
        this.departure = departure;
        this.arrival = arrival;
        this.time = time;
        this.price = price;
        this.capacity = capacity;
        this.seats = seats;
    }
    public Flight(String id, String departure, String arrival, String time, double price, int capacity) {
        this(id, departure, arrival, time, price, capacity, capacity);
    }
    public Flight() { this("UNKNOWN",null, null,"",0.0,0); }

    public String getId() { return this.id; }
    public void setId(String id) {
        this.id = id;
    }

    public String getDeparture() { return this.departure; }
    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getArrival() {
        return this.arrival;
    }
    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public String getTime() { return this.time; }
    public void setTime(String time) {
        this.time = time;
    }

    public double getPrice() {
        return this.price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    public int getCapacity() {
        return this.capacity;
    }
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getSeats() { return this.seats; }
    public void addSeats(int count) { this.seats += count; }
    public void reserveSeats(int count) { this.seats -= count; }
}
