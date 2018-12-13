package com.example.brettstevenson.airlineticketreservationsystem.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import com.example.brettstevenson.airlineticketreservationsystem.Customer;
import com.example.brettstevenson.airlineticketreservationsystem.Flight;
import com.example.brettstevenson.airlineticketreservationsystem.Reservation;
import com.example.brettstevenson.airlineticketreservationsystem.Transaction;

public class AirlineHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "airline.db";
    private static final int VERSION = 1;
    private SQLiteDatabase db;

    public AirlineHelper(Context context) { super(context, DATABASE_NAME, null, VERSION); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + AirlineDbSchema.CustomerTable.NAME + "(" +
                AirlineDbSchema.CustomerTable.Cols.UUID + ", " +
                AirlineDbSchema.CustomerTable.Cols.USERNAME + ", " +
                AirlineDbSchema.CustomerTable.Cols.PASSWORD + ", " +
                AirlineDbSchema.CustomerTable.Cols.ADMIN + ", " +
                AirlineDbSchema.CustomerTable.Cols.RESERVATIONS + ")"
        );
        db.execSQL("create table " + AirlineDbSchema.FlightTable.NAME + "(" +
                AirlineDbSchema.FlightTable.Cols.ID + ", " +
                AirlineDbSchema.FlightTable.Cols.DEPARTURE + ", " +
                AirlineDbSchema.FlightTable.Cols.ARRIVAL + ", " +
                AirlineDbSchema.FlightTable.Cols.TIME + ", " +
                AirlineDbSchema.FlightTable.Cols.PRICE + ", " +
                AirlineDbSchema.FlightTable.Cols.CAPACITY + ")"
        );
        db.execSQL("create table " + AirlineDbSchema.ReservationTable.NAME + "(" +
                AirlineDbSchema.ReservationTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                AirlineDbSchema.ReservationTable.Cols.CUSTOMER + ", " +
                AirlineDbSchema.ReservationTable.Cols.FLIGHT + ", " +
                AirlineDbSchema.ReservationTable.Cols.TICKETS + ", " +
                AirlineDbSchema.ReservationTable.Cols.PRICE + ")"
        );
        db.execSQL("create table " + AirlineDbSchema.TransactionTable.NAME + "(" +
                AirlineDbSchema.TransactionTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                AirlineDbSchema.TransactionTable.Cols.CONTENTS + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

    public long insertCustomer(Customer customer) {
        ContentValues cv = new ContentValues();
        cv.put(AirlineDbSchema.CustomerTable.Cols.UUID, customer.getId().toString());
        cv.put(AirlineDbSchema.CustomerTable.Cols.USERNAME, customer.getUsername());
        cv.put(AirlineDbSchema.CustomerTable.Cols.PASSWORD, customer.getPassword());
        cv.put(AirlineDbSchema.CustomerTable.Cols.ADMIN, customer.isAdmin() ? 1 : 0 );
        if(customer.getReservations().size() > 0) {
            cv.put(AirlineDbSchema.CustomerTable.Cols.RESERVATIONS, createReservationString(customer.getReservations().keySet()));
        }
        db = this.getWritableDatabase();
        //name of the table, nullColumnHack, data to add
        return db.insert(AirlineDbSchema.CustomerTable.NAME, null, cv);
    }

    public String createReservationString(Set<Integer> res) {
        final String LIST_SEPARATOR = "__,__";
        StringBuilder stringBuilder = new StringBuilder();
        for (int index : res) {
            stringBuilder.append(index).append(LIST_SEPARATOR);
        }
        // Remove last separator
        stringBuilder.setLength(stringBuilder.length() - LIST_SEPARATOR.length());
        return stringBuilder.toString();
    }

    public HashMap<Integer, Reservation> translateReservationString(String resString) {
        HashMap<Integer, Reservation> reservations = getReservations();
        HashMap<Integer, Reservation> customerRes = new HashMap<>();
        for(String s : resString.split("__,__")) {
            customerRes.put(Integer.parseInt(s), reservations.get(Integer.parseInt(s)));
        }
        return customerRes;
    }

    public boolean updateCustomer(String uuid, Customer customer) {
        ContentValues cv = new ContentValues();
        cv.put(AirlineDbSchema.CustomerTable.Cols.UUID, customer.getId().toString());
        cv.put(AirlineDbSchema.CustomerTable.Cols.USERNAME, customer.getUsername());
        cv.put(AirlineDbSchema.CustomerTable.Cols.PASSWORD, customer.getPassword());
        cv.put(AirlineDbSchema.CustomerTable.Cols.ADMIN, customer.isAdmin() ? 1 : 0 );
        if(customer.getReservations().size() > 0) {
            cv.put(AirlineDbSchema.CustomerTable.Cols.RESERVATIONS, createReservationString(customer.getReservations().keySet()));
        }
        try {
            db = this.getWritableDatabase();
            db.update(AirlineDbSchema.CustomerTable.NAME, cv, AirlineDbSchema.CustomerTable.Cols.UUID + " = ?",
                    new String[]{ uuid });
            // prevent SQL injection!\
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean updateCustomer(String uuid, ContentValues values) {
        try {
            db = this.getWritableDatabase();
            db.update(AirlineDbSchema.CustomerTable.NAME, values, AirlineDbSchema.CustomerTable.Cols.UUID + " = ?",
                    new String[]{ uuid });
            // prevent SQL injection!\
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean deleteCustomer(String uuid) {
        try {
            db = this.getWritableDatabase();
            db.delete(AirlineDbSchema.CustomerTable.NAME, AirlineDbSchema.CustomerTable.Cols.UUID + " = ?",
                    new String[]{ uuid });
            // prevent SQL injection!
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Cursor queryCustomerDB(String DBName, String whereClause, String[] whereArgs) {
        db = this.getWritableDatabase();
        try {
            return db.query(AirlineDbSchema.CustomerTable.NAME, null, whereClause, whereArgs, null,  null,  null);
        } catch (Exception e) {
            return null;
        }
    }

    public HashMap<String, Customer> getCustomers() {
        HashMap<String, Customer> customers = new HashMap<>();
        AirlineCursorWrapper cursor = new AirlineCursorWrapper(this.queryCustomerDB(AirlineDbSchema.CustomerTable.NAME, null,null));
        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                customers.put(cursor.getCustomer().getUsername(), cursor.getCustomer());
                cursor.moveToNext();
            }
        } finally {
            if(cursor != null)
                cursor.close();
        }
        return customers;
    }

    public long insertFlight(Flight flight) {
        ContentValues cv = new ContentValues();
        cv.put(AirlineDbSchema.FlightTable.Cols.ID, flight.getId());
        cv.put(AirlineDbSchema.FlightTable.Cols.DEPARTURE, flight.getDeparture());
        cv.put(AirlineDbSchema.FlightTable.Cols.ARRIVAL, flight.getArrival());
        cv.put(AirlineDbSchema.FlightTable.Cols.TIME, flight.getTime());
        cv.put(AirlineDbSchema.FlightTable.Cols.PRICE, flight.getPrice());
        cv.put(AirlineDbSchema.FlightTable.Cols.CAPACITY, flight.getCapacity());
        db = this.getWritableDatabase();
        // name of the table, nullColumnHack, data to add
        return db.insert(AirlineDbSchema.FlightTable.NAME, null, cv);
    }

    public boolean updateFlight(String id, ContentValues values) {
        try {
            db = this.getWritableDatabase();
            db.update(AirlineDbSchema.FlightTable.NAME, values, AirlineDbSchema.FlightTable.Cols.ID + " = ?",
                    new String[]{ id });
            // prevent SQL injection!
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean deleteFlight(String id) {
        try {
            db = this.getWritableDatabase();
            db.delete(AirlineDbSchema.FlightTable.NAME, AirlineDbSchema.FlightTable.Cols.ID + " = ?",
                    new String[]{ id });
            // prevent SQL injection!
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Cursor queryFlightsDB(String DBName, String whereClause, String[] whereArgs) {
        db = this.getWritableDatabase();
        try {
            return db.query(AirlineDbSchema.FlightTable.NAME, null, whereClause, whereArgs, null,  null,  null);
        } catch (Exception e) {
            return null;
        }
    }

    public HashMap<String, Flight> getFlights() {
        HashMap<String, Flight> flights = new HashMap<>();
        AirlineCursorWrapper cursor = new AirlineCursorWrapper(this.queryFlightsDB(AirlineDbSchema.FlightTable.NAME, null,null));
        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                Flight flight = cursor.getFlight();
                flights.put(flight.getId(), flight);
                cursor.moveToNext();
            }
        } finally {
            if(cursor != null)
                cursor.close();
        }
        return flights;
    }

    public long insertReservation(Reservation reservation) {
        ContentValues cv = new ContentValues();
        cv.put(AirlineDbSchema.ReservationTable.Cols.ID, reservation.getId());
        cv.put(AirlineDbSchema.ReservationTable.Cols.CUSTOMER, reservation.getCustomer());
        cv.put(AirlineDbSchema.ReservationTable.Cols.FLIGHT, reservation.getFlight().getId());
        cv.put(AirlineDbSchema.ReservationTable.Cols.TICKETS, reservation.getTicketCount());
        cv.put(AirlineDbSchema.ReservationTable.Cols.PRICE, reservation.getPrice());
        db = this.getWritableDatabase();
        // name of the table, nullColumnHack, data to add
        return db.insert(AirlineDbSchema.ReservationTable.NAME, null, cv);
    }

    public boolean updateReservation(int id, ContentValues values) {
        try {
            db = this.getWritableDatabase();
            db.update(AirlineDbSchema.ReservationTable.NAME, values, AirlineDbSchema.ReservationTable.Cols.ID + " = ?",
                    new String[]{ Integer.toString(id) });
            // prevent SQL injection!
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean deleteReservation(int id) {
        try {
            db = this.getWritableDatabase();
            db.delete(AirlineDbSchema.ReservationTable.NAME, AirlineDbSchema.ReservationTable.Cols.ID + " = ?",
                    new String[]{ Integer.toString(id) });
            // prevent SQL injection!
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Cursor queryReservationsDB(String DBName, String whereClause, String[] whereArgs) {
        db = this.getWritableDatabase();
        try {
            return db.query(AirlineDbSchema.ReservationTable.NAME, null, whereClause, whereArgs, null,  null,  null);
        } catch (Exception e) {
            return null;
        }
    }

    public HashMap<Integer, Reservation> getReservations() {
        HashMap<Integer, Reservation> reservations = new HashMap<>();
        AirlineCursorWrapper cursor = new AirlineCursorWrapper(this.queryReservationsDB(AirlineDbSchema.ReservationTable.NAME, null,null));
        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                Reservation reservation = cursor.getReservation(getFlights());
                reservations.put(reservation.getId(), reservation);
                cursor.moveToNext();
            }
        } finally {
            if(cursor != null)
                cursor.close();
        }
        return reservations;
    }

    public long insertTransaction(Transaction transaction) {
        ContentValues cv = new ContentValues();
        // cv.put(AirlineDbSchema.TransactionTable.Cols.ID, transaction.getId());
        cv.put(AirlineDbSchema.TransactionTable.Cols.CONTENTS, transaction.toString());
        db = this.getWritableDatabase();
        // name of the table, nullColumnHack, data to add
        return db.insert(AirlineDbSchema.TransactionTable.NAME, null, cv);
    }

    public Cursor queryTransactionsDB(String DBName, String whereClause, String[] whereArgs) {
        db = this.getWritableDatabase();
        try {
            return db.query(AirlineDbSchema.TransactionTable.NAME, null, whereClause, whereArgs, null,  null,  null);
        } catch (Exception e) {
            return null;
        }
    }

    public ArrayList<String> getTransactions() {
        ArrayList<String> transactions = new ArrayList<>();
        AirlineCursorWrapper cursor = new AirlineCursorWrapper(this.queryTransactionsDB(AirlineDbSchema.TransactionTable.NAME, null,null));
        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                String transaction = cursor.getTransaction();
                transactions.add(transaction);
                cursor.moveToNext();
            }
        } finally {
            if(cursor != null)
                cursor.close();
        }
        return transactions;
    }
}
