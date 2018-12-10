package com.example.brettstevenson.airlineticketreservationsystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import java.util.HashMap;
import android.database.sqlite.SQLiteDatabase;
import com.example.brettstevenson.airlineticketreservationsystem.Database.AirlineCursorWrapper;
import com.example.brettstevenson.airlineticketreservationsystem.Database.AirlineDbSchema;
import com.example.brettstevenson.airlineticketreservationsystem.Database.AirlineHelper;

public class FlightList {

    private static FlightList sFlight;
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private AirlineHelper mAirlineHelper;
    private HashMap<String, Flight> mFlights;

    public static FlightList get(Context context) {
        if(sFlight == null)
            sFlight = new FlightList(context);
        return sFlight;
    }

    private FlightList(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new AirlineHelper(mContext).getWritableDatabase();
        mAirlineHelper = new AirlineHelper(mContext);
        Flight[] flights = { new Flight("otter101", "Monterey", "Los Angeles", "10:00 AM", 150.00, 10),
                             new Flight("otter102", "Los Angeles", "Monterey", "1:00 PM", 150.00, 10),
                             new Flight("otter201", "Monterey", "Seattle", "11:00 AM", 200.50, 5),
                             new Flight("otter202", "Seattle", "Monterey", "2:00 PM", 200.50, 5),
                             new Flight("otter205", "Monterey", "Seattle", "3:00 PM", 150.00, 15)};
        for(Flight flight : flights) {
            addFlight(flight);
        }
        mFlights = getFlights();
    }

    public void addFlight(Flight f) {
        ContentValues values = getContentValues(f);
//        mDatabase.insert(values);
        mAirlineHelper.insertFlight(f);
    }

    public HashMap<String, Flight> getFlights() {
        /**
         * This should live in FlightListHelper. Not here.
         * Edit: and now it does.
         * Edit: and now it is broken. Putting it back. Figure it out later.
         * Edit: if you are gonna use try catches, LOG WHEN YOU CATCH AN ERROR!
         * turns out I was casting to a TudoCursorWrapper where such things aren't allowed.
         */
//        AirlineCursorWrapper cursor = queryFlights(null,null);
//        List<Flight> todos = new ArrayList<>();
//
//        try{
//            cursor.moveToFirst();
//            while(!cursor.isAfterLast()){
//                todos.add(cursor.getFlight());
//                cursor.moveToNext();
//            }
//        }finally{
//                cursor.close();
//        }
//        return todos;
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

    @Deprecated //SHould call query DB directly from the helper
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
        return values;
    }
}
