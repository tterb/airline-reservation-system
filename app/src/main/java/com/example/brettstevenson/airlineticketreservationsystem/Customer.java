package com.example.brettstevenson.airlineticketreservationsystem;

import java.util.HashMap;
import java.util.UUID;

public class Customer {
    private UUID mId;
    private String mUsername;
    private String mPassword;
    private boolean mAdmin;
    private String mReservationString = "";
    private HashMap<Integer, Reservation> mReservations;

    public Customer(UUID uuid, String username, String password, boolean admin, String reservationString) {
        this.mId = UUID.randomUUID();
        this.mUsername = username;
        this.mPassword = password;
        this.mAdmin = admin;
        this.mReservationString = reservationString;
        this.mReservations = new HashMap<>();
    }
    public Customer(UUID uuid, String username, String password, boolean admin) {
        this.mId = UUID.randomUUID();
        this.mUsername = username;
        this.mPassword = password;
        this.mAdmin = admin;
        this.mReservations = new HashMap<>();
    }
    public Customer(String username, String password, boolean admin) {
        this(UUID.randomUUID(), username, password, admin);
    }
    public Customer(String username, String password) { this(username, password, false); }
    public Customer() { this("", "", false); }

    public UUID getId() { return mId; }

    public String getUsername() { return this.mUsername; }
    public void setUsername(String username) { this.mUsername = username; }

    public String getPassword() { return this.mPassword; }
    public void setPassword(String password) { this.mPassword = password; }

    public boolean isAdmin() { return this.mAdmin; }

    public HashMap<Integer, Reservation> getReservations() { return this.mReservations; }
    public void addReservation(Reservation res) {
        if(this.mReservations.get(res.getId()) == null)
            this.mReservations.put(res.getId(), res);
    }
    public void cancelReservation(Reservation res) {
        if(this.mReservations.get(res.getId()) != null)
            this.mReservations.remove(res.getId(), res);
    }

    public String getReservationString() {
        return mReservationString;
    }

    public String toString() { return mUsername + " " + mPassword; }
}
