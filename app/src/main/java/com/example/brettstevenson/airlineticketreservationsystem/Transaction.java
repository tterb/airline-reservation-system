package com.example.brettstevenson.airlineticketreservationsystem;

import java.util.Date;
import java.text.SimpleDateFormat;

public class Transaction {

    private String type;
    private String customer;
    private String date;
    private String time;

    public Transaction(String type, String customer) {
        this.type = type;
        this.customer = customer;
        setDateTime();
    }
    public Transaction() { this("", null); }

    public String getType() { return this.type; }

    public String getCustomer() { return this.customer; }

    public String getDate() { return this.date; }
    public String getTime() { return this.time; }

    public void setDateTime() {
        Date date = new Date();
        this.date = new SimpleDateFormat("MM/dd/yyyy").format(date);
        this.time = new SimpleDateFormat("hh:mm a").format(date);
    }
}
