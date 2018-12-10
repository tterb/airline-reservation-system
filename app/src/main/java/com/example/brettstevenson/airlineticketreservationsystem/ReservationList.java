package com.example.brettstevenson.airlineticketreservationsystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import java.util.HashMap;
import android.database.sqlite.SQLiteDatabase;
import com.example.brettstevenson.airlineticketreservationsystem.Database.AirlineCursorWrapper;
import com.example.brettstevenson.airlineticketreservationsystem.Database.AirlineDbSchema;
import com.example.brettstevenson.airlineticketreservationsystem.Database.AirlineHelper;

public class ReservationList {

    private static ReservationList sReservation;
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private AirlineHelper mAirlineHelper;
    private HashMap<Integer, Reservation> mReservations;

    public static ReservationList get(Context context) {
        if(sReservation == null)
            sReservation = new ReservationList(context);
        return sReservation;
    }

    private ReservationList(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new AirlineHelper(mContext).getWritableDatabase();
        mAirlineHelper = new AirlineHelper(mContext);
        mReservations = getReservations();
    }

    public void addReservation(Reservation f) {
        ContentValues values = getContentValues(f);
//        mDatabase.insert(values);
        mAirlineHelper.insertReservation(f);
    }

    public HashMap<Integer, Reservation> getReservations() {
        return mAirlineHelper.getReservations();
    }

    public Reservation getReservation(int id) {
        AirlineCursorWrapper cursor = queryReservations(
                AirlineDbSchema.ReservationTable.Cols.ID + " = ? ",
                new String[] { String.valueOf(id) }
        );
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getReservation(mAirlineHelper.getFlights());
        } finally {
            cursor.close();
        }
    }

//    public void updateList(){
//        mReservations = getReservations();
//    }

    public void updateReservation(Reservation reservation) {
        int id = reservation.getId();
        ContentValues values = getContentValues(reservation);
        mAirlineHelper.updateReservation(id, values);
    }

    public void deleteReservation(Reservation reservation) {
        int id = reservation.getId();
        mAirlineHelper.deleteReservation(id);
    }

    @Deprecated //Should call query DB directly from the helper
    private AirlineCursorWrapper queryReservations(String whereClause, String[] whereArgs) {
        Cursor cursor = mAirlineHelper.queryReservationsDB(AirlineDbSchema.ReservationTable.NAME,whereClause, whereArgs);
        return new AirlineCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Reservation reservation) {
        ContentValues values = new ContentValues();
        values.put(AirlineDbSchema.ReservationTable.Cols.ID, reservation.getId());
        values.put(AirlineDbSchema.ReservationTable.Cols.CUSTOMER, reservation.getCustomer());
        values.put(AirlineDbSchema.ReservationTable.Cols.FLIGHT, reservation.getFlight().getId());
        values.put(AirlineDbSchema.ReservationTable.Cols.TICKETS, reservation.getTicketCount());
        values.put(AirlineDbSchema.ReservationTable.Cols.PRICE, reservation.getPrice());
        return values;
    }
}
