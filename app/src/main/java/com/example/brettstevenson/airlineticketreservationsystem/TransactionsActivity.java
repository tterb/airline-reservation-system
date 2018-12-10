package com.example.brettstevenson.airlineticketreservationsystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.brettstevenson.airlineticketreservationsystem.Database.AirlineHelper;
import java.util.ArrayList;
import java.util.Collections;

public class TransactionsActivity extends AppCompatActivity {

    private static final String TAG =  "AirlineDemo";
    private AirlineHelper airlineHelper;
    private ArrayList<String> transactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "TransactionsActivity created");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        ListView listView = findViewById(R.id.transaction_list);
        airlineHelper = new AirlineHelper(this.getApplicationContext());
        transactions = airlineHelper.getTransactions();
        // Put newer transactions at front of ArrayList
        Collections.reverse(transactions);
        if(transactions.size() > 0) {
            Log.d(TAG, transactions.size()+"Transactions!");
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, transactions);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
}
