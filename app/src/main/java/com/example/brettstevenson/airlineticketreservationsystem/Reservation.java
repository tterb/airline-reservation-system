package com.example.brettstevenson.airlineticketreservationsystem;

public class Reservation extends Transaction {

    private int id;
    private String customer;
    private Flight flight;
    private int ticketCount;
    private double price;
    private boolean cancelled;
    private static int reservationCount = 0;

    public Reservation(int id, String customer, Flight flight, int ticketCount, double price) {
        super("Reservation", customer);
        this.id = id;
        this.customer = customer;
        this.flight = flight;
        this.ticketCount = ticketCount;
        this.price = price;
        this.cancelled = false;
    }
    public Reservation(String customer, Flight flight, int ticketCount, double price) {
        this(incrementResId(), customer, flight, ticketCount, price);
    }
    // Only called on reservation cancellation
    public Reservation(Reservation res) {
        super("Cancel Reservation", res.getCustomer());
        this.id = res.getId();
        this.customer = res.getCustomer();
        this.flight = res.getFlight();
        this.ticketCount = res.getTicketCount();
        this.price = res.getPrice();
        this.cancelled = true;
    }
    public Reservation() { this(null, null, 0, 0.0); }

    public int getId() { return this.id; }
    public static int incrementResId() { return reservationCount++; }
//    public int getTransactionId() { return super.getId(); }

    public String getCustomer() {
        return this.customer;
    }
    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public Flight getFlight() {
        return this.flight;
    }
    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public int getTicketCount() {
        return this.ticketCount;
    }
    public void setTicketCount(int ticketCount) {
        this.ticketCount = ticketCount;
    }

    public double getPrice() { return this.price; }
    public void setPrice() { this.price = price; }

    public boolean isCancelled() { return cancelled; }
    public void cancel() { this.cancelled = true; }

    @Override
    public String toString() {
        return "Transaction type: "+super.getType()+"\nCustomer's Username: "+this.getCustomer()
                +"\nFlight number: "+this.flight.getId()+"\nDeparture: "+this.flight.getDeparture()
                +", "+this.flight.getTime()+"\nArrival: "+this.flight.getArrival()+"\nNumber of Tickets: "
                +this.getTicketCount()+"\nReservation number: "+this.id+"\nTotal Amount: "+this.getPrice()
                +"\nTransaction Date: "+super.getDate()+"\nTransaction Time: "+super.getTime();
    }
}
