package com.example.brettstevenson.airlineticketreservationsystem;

import android.content.Context;
import android.database.Cursor;
import com.example.brettstevenson.airlineticketreservationsystem.Database.AirlineCursorWrapper;
import com.example.brettstevenson.airlineticketreservationsystem.Database.AirlineDbSchema;
import com.example.brettstevenson.airlineticketreservationsystem.Database.AirlineHelper;
import java.util.ArrayList;

public class TransactionList {

    private static TransactionList sTransaction;
    private Context mContext;
    private AirlineHelper mAirlineHelper;
    private ArrayList<String> mTransactions;

    public static TransactionList get(Context context) {
        if(sTransaction == null)
            sTransaction = new TransactionList(context);
        return sTransaction;
    }

    private TransactionList(Context context) {
        mContext = context.getApplicationContext();
        mAirlineHelper = new AirlineHelper(mContext);
        mTransactions = getTransactions();
    }

    public void addTransaction(Transaction c) {
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

    @Deprecated // Should call query DB directly from the helper
    private AirlineCursorWrapper queryTransactions(String whereClause, String[] whereArgs) {
        Cursor cursor = mAirlineHelper.queryTransactionsDB(AirlineDbSchema.TransactionTable.NAME, whereClause, whereArgs);
        return new AirlineCursorWrapper(cursor);
    }
}
