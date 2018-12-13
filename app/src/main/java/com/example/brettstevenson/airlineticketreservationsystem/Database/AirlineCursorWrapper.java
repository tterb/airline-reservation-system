package com.example.brettstevenson.airlineticketreservationsystem.Database;

import android.database.Cursor;
import android.database.CursorWrapper;
import com.example.brettstevenson.airlineticketreservationsystem.Customer;
import com.example.brettstevenson.airlineticketreservationsystem.Flight;
import com.example.brettstevenson.airlineticketreservationsystem.Reservation;
import java.util.HashMap;
import java.util.UUID;

public class AirlineCursorWrapper  extends CursorWrapper {

    /**
     *
     * @param cursor
     * Our constructor creates a so called 'thin wrapper' that has all the same functionality
     * of the parent. The benefit is that we may now add our own methods. So yay inheritance.
     */
    public AirlineCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public Customer getCustomer() {
        String uuid = getString(getColumnIndex(AirlineDbSchema.CustomerTable.Cols.UUID));
        String username = getString(getColumnIndex(AirlineDbSchema.CustomerTable.Cols.USERNAME));
        String password = getString(getColumnIndex(AirlineDbSchema.CustomerTable.Cols.PASSWORD));
        boolean isAdmin = (getInt(getColumnIndex(AirlineDbSchema.CustomerTable.Cols.ADMIN)) == 1);
        String reservations = getString(getColumnIndex(AirlineDbSchema.CustomerTable.Cols.RESERVATIONS));
        return new Customer(UUID.fromString(uuid), username, password, isAdmin, reservations);
    }

    public Flight getFlight() {
        String id = getString(getColumnIndex(AirlineDbSchema.FlightTable.Cols.ID));
        String destination = getString(getColumnIndex(AirlineDbSchema.FlightTable.Cols.DEPARTURE));
        String arrival = getString(getColumnIndex(AirlineDbSchema.FlightTable.Cols.ARRIVAL));
        String time = getString(getColumnIndex(AirlineDbSchema.FlightTable.Cols.TIME));
        double price = getDouble(getColumnIndex(AirlineDbSchema.FlightTable.Cols.PRICE));
        int capacity = getInt(getColumnIndex(AirlineDbSchema.FlightTable.Cols.CAPACITY));
        return new Flight(id, destination, arrival, time, price, capacity);
    }

    public Reservation getReservation(HashMap<String, Flight> flights) {
        int id = getInt(getColumnIndex(AirlineDbSchema.ReservationTable.Cols.ID));
        String customer = getString(getColumnIndex(AirlineDbSchema.ReservationTable.Cols.CUSTOMER));
        String flightId = getString(getColumnIndex(AirlineDbSchema.ReservationTable.Cols.FLIGHT));
        int tickets = getInt(getColumnIndex(AirlineDbSchema.ReservationTable.Cols.TICKETS));
        double price = getDouble(getColumnIndex(AirlineDbSchema.ReservationTable.Cols.PRICE));
        return new Reservation(id, customer, flights.get(flightId), tickets, price);
    }

    public String getTransaction() {
        String contents = getString(getColumnIndex(AirlineDbSchema.TransactionTable.Cols.CONTENTS));
        return contents;
    }
}
