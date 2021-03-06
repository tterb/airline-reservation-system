package com.example.brettstevenson.airlineticketreservationsystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import java.util.HashMap;
import com.example.brettstevenson.airlineticketreservationsystem.Database.AirlineCursorWrapper;
import com.example.brettstevenson.airlineticketreservationsystem.Database.AirlineDbSchema;
import com.example.brettstevenson.airlineticketreservationsystem.Database.AirlineHelper;

public class FlightList {

    private static FlightList sFlight;
    private Context mContext;
    private AirlineHelper mAirlineHelper;
    private HashMap<String, Flight> mFlights;

    public static FlightList get(Context context) {
        if(sFlight == null)
            sFlight = new FlightList(context);
        return sFlight;
    }

    private FlightList(Context context) {
        mContext = context.getApplicationContext();
        mAirlineHelper = new AirlineHelper(mContext);
        // Loading flights here since the SQLite wouldn't work
        Flight[] flights = { new Flight("Otter101", "Monterey", "Los Angeles", "10:00 AM", 150.00, 10),
                             new Flight("Otter102", "Los Angeles", "Monterey", "1:00 PM", 150.00, 10),
                             new Flight("Otter201", "Monterey", "Seattle", "11:00 AM", 200.50, 5),
                             new Flight("Otter202", "Seattle", "Monterey", "2:00 PM", 200.50, 5),
                             new Flight("Otter205", "Monterey", "Seattle", "3:00 PM", 150.00, 15)};
        for(Flight flight : flights) {
            addFlight(flight);
        }
        mFlights = getFlights();
    }

    public void addFlight(Flight f) {
        mAirlineHelper.insertFlight(f);
    }

    public HashMap<String, Flight> getFlights() {
        return  mAirlineHelper.getFlights();
    }

    public Flight getFlight(String id) {
        AirlineCursorWrapper cursor = queryFlights(
                AirlineDbSchema.FlightTable.Cols.ID + " = ? ",
                new String[] { id }
        );
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getFlight();
        } finally {
            cursor.close();
        }
    }

    public void updateList(){
        mFlights = getFlights();
    }

    public void updateFlight(Flight flight) {
        String id = flight.getId();
        ContentValues values = getContentValues(flight);
        mAirlineHelper.updateFlight(id, values);
    }

    public void deleteFlight(Flight flight) {
        String id = String.valueOf(flight.getId());
        mAirlineHelper.deleteFlight(id);
    }

    @Deprecated //Should call query DB directly from the helper
    private AirlineCursorWrapper queryFlights(String whereClause, String[] whereArgs) {
        Cursor cursor = mAirlineHelper.queryFlightsDB(AirlineDbSchema.FlightTable.NAME,whereClause, whereArgs);
        return new AirlineCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Flight flight) {
        ContentValues values = new ContentValues();
        values.put(AirlineDbSchema.FlightTable.Cols.ID, flight.getId());
        values.put(AirlineDbSchema.FlightTable.Cols.DEPARTURE, flight.getDeparture());
        values.put(AirlineDbSchema.FlightTable.Cols.ARRIVAL, flight.getArrival());
        values.put(AirlineDbSchema.FlightTable.Cols.TIME, flight.getTime());
        values.put(AirlineDbSchema.FlightTable.Cols.PRICE, flight.getPrice());
        values.put(AirlineDbSchema.FlightTable.Cols.CAPACITY, flight.getCapacity());
        values.put(AirlineDbSchema.FlightTable.Cols.SEATS, flight.getSeats());
        return values;
    }
}
