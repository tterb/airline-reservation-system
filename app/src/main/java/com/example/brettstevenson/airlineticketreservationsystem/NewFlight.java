package com.example.brettstevenson.airlineticketreservationsystem;

public class NewFlight extends Transaction {

    private Flight flight;

    public NewFlight(String customer, Flight flight) {
        super("Flight Added", customer);
        super.setDateTime();
        this.flight = flight;
    }
    public NewFlight() { this("", null); }

    @Override
    public String toString() {
        return "Transaction type: "+super.getType()+"\nCustomer's Username: "+super.getCustomer()
                +"\nFlight number: "+this.flight.getId()+"\nDeparture: "+this.flight.getDeparture()
                +", "+this.flight.getTime()+"\nArrival: "+this.flight.getArrival()
                +"\nTransaction Date: "+super.getDate()+"\nTransaction Time: "+super.getTime();
    }
}
