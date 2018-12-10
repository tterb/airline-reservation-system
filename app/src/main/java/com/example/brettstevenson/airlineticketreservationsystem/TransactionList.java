package com.example.brettstevenson.airlineticketreservationsystem;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.brettstevenson.airlineticketreservationsystem.Database.AirlineCursorWrapper;
import com.example.brettstevenson.airlineticketreservationsystem.Database.AirlineDbSchema;
import com.example.brettstevenson.airlineticketreservationsystem.Database.AirlineHelper;
import java.util.ArrayList;

public class TransactionList {

    private Context mContext;
    private SQLiteDatabase mDatabase;
    private AirlineHelper mAirlineHelper;
    private ArrayList<String> mTransactions;
    private static TransactionList sTransaction;

    public static TransactionList get(Context context) {
        if(sTransaction == null)
            sTransaction = new TransactionList(context);
        return sTransaction;
    }

    private TransactionList(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new AirlineHelper(mContext).getWritableDatabase();
        mAirlineHelper = new AirlineHelper(mContext);
        mTransactions = getTransactions();
    }

    public void addTransaction(Transaction c) {
//        ContentValues values = getContentValues(c);
        mAirlineHelper.insertTransaction(c);
    }

    public ArrayList<String> getTransactions() {
        return mAirlineHelper.getTransactions();
    }

    public String getTransaction(int id) {
        AirlineCursorWrapper cursor = queryTransactions(
                AirlineDbSchema.TransactionTable.Cols.ID + " = ? ",
                new String[] { String.valueOf(id) }
        );
        try {
            if (cursor.getCount() == 0)
                return null;
            cursor.moveToFirst();
            return cursor.getTransaction();
        } finally {
            cursor.close();
        }
    }

    public void updateList() {
        mTransactions = getTransactions();
    }

//    public void updateTransaction(Transaction transaction) {
//        String id = String.valueOf(transaction.getId());
//        ContentValues values = getContentValues(transaction);
//        mAirlineHelper.updateTransaction(id, values);
//    }

//    public void deleteTransaction(Transaction transaction) {
//        String id = String.valueOf(transaction.getId());
//        mAirlineHelper.deleteTransaction(id);
//    }

    @Deprecated // Should call query DB directly from the helper
    private AirlineCursorWrapper queryTransactions(String whereClause, String[] whereArgs) {
        Cursor cursor = mAirlineHelper.queryTransactionsDB(AirlineDbSchema.TransactionTable.NAME, whereClause, whereArgs);
        return new AirlineCursorWrapper(cursor);
    }

//    private static ContentValues getContentValues(Transaction transaction) {
//        ContentValues values = new ContentValues();
//        values.put(AirlineDbSchema.TransactionTable.Cols.ID, String.valueOf(transaction.getId()));
//        values.put(AirlineDbSchema.TransactionTable.Cols.CONTENTS, transaction.toString());
//        return values;
//    }
}
