package com.example.brettstevenson.airlineticketreservationsystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
import com.example.brettstevenson.airlineticketreservationsystem.Database.AirlineCursorWrapper;
import com.example.brettstevenson.airlineticketreservationsystem.Database.AirlineDbSchema;
import com.example.brettstevenson.airlineticketreservationsystem.Database.AirlineHelper;

public class CustomerList {

    private static CustomerList sCustomer;
    private Context mContext;
    private AirlineHelper mAirlineHelper;
    private HashMap<String, Customer> mCustomers;

    public static CustomerList get(Context context) {
        if(sCustomer == null)
            sCustomer = new CustomerList(context);
        return sCustomer;
    }

    private CustomerList(Context context) {
        mContext = context.getApplicationContext();
        mAirlineHelper = new AirlineHelper(mContext);
        // Loading customers here since the SQLite wouldn't work
        Customer[] customers = { new Customer("alice5", "csumb100", false),
                                 new Customer("brian77", "123ABC", false),
                                 new Customer("chris21", "CHRIS21", false),
                                 new Customer("admin2", "admin2", true) };
        for(Customer customer : customers) {
            addCustomer(customer);
        }
        mCustomers = getCustomers();
    }

    public void addCustomer(Customer c) {
        mAirlineHelper.insertCustomer(c);
    }

    public HashMap<String, Customer> getCustomers() {
        return mAirlineHelper.getCustomers();
    }

    public Customer getCustomer(UUID id) {
        AirlineCursorWrapper cursor = queryCustomers(
                AirlineDbSchema.CustomerTable.Cols.UUID + " = ? ",
                new String[] { id.toString() }
        );
        try {
            if (cursor.getCount() == 0)
                return null;
            cursor.moveToFirst();
            return cursor.getCustomer();
        } finally {
            cursor.close();
        }
    }
    public Customer getCustomer(String username) {
        AirlineCursorWrapper cursor = queryCustomers(
                AirlineDbSchema.CustomerTable.Cols.USERNAME + " = ? ",
                new String[] { username }
        );
        try {
            if (cursor.getCount() == 0)
                return null;
            cursor.moveToFirst();
            return cursor.getCustomer();
        } finally {
            cursor.close();
        }
    }

    public void updateList() {
        mCustomers = getCustomers();
    }

    public void updateCustomer(Customer customer) {
        String uuid = String.valueOf(customer.getId());
        ContentValues values = getContentValues(customer);
        mAirlineHelper.updateCustomer(uuid, values);
    }

    public void deleteCustomer(Customer customer) {
        String uuid = String.valueOf(customer.getId());
        mAirlineHelper.deleteCustomer(uuid);
    }

    @Deprecated // Should call query DB directly from the helper
    private AirlineCursorWrapper queryCustomers(String whereClause, String[] whereArgs) {
        Cursor cursor = mAirlineHelper.queryCustomerDB(AirlineDbSchema.CustomerTable.NAME,whereClause, whereArgs);
        return new AirlineCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Customer customer) {
        ContentValues values = new ContentValues();
        values.put(AirlineDbSchema.CustomerTable.Cols.UUID, String.valueOf(customer.getId()));
        values.put(AirlineDbSchema.CustomerTable.Cols.USERNAME, customer.getUsername());
        values.put(AirlineDbSchema.CustomerTable.Cols.PASSWORD, customer.getPassword());
        values.put(AirlineDbSchema.CustomerTable.Cols.ADMIN, customer.isAdmin() ? 1 : 0);
        if(customer.getReservations().size() > 0) {
            values.put(AirlineDbSchema.CustomerTable.Cols.RESERVATIONS, createReservationString(customer.getReservations().keySet()));
        }
        return values;
    }

    public static String createReservationString(Set<Integer> res) {
        final String LIST_SEPARATOR = "__,__";
        StringBuilder stringBuilder = new StringBuilder();
        for (int index : res) {
            stringBuilder.append(index).append(LIST_SEPARATOR);
        }
        // Remove last separator
        stringBuilder.setLength(stringBuilder.length() - LIST_SEPARATOR.length());
        return stringBuilder.toString();
    }
}
