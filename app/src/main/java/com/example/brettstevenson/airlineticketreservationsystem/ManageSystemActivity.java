package com.example.brettstevenson.airlineticketreservationsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class ManageSystemActivity extends Activity implements OnClickListener {

    private static final String TAG =  "AirlineDemo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_system);
        // Set up a click listener for the "Transactions" primary_button.
        View transactionButton = findViewById(R.id.transactions_button);
        transactionButton.setOnClickListener(this);
        // Set up a click listener for the "Add Flight" primary_button.
        View addFlightButton = findViewById(R.id.add_flight_button);
        addFlightButton.setOnClickListener(this);
        // Set up a click listener for the "Main Menu" primary_button.
        View mainMenuButton = findViewById(R.id.main_menu_button);
        mainMenuButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.transactions_button) {
            startActivity(new Intent(this, TransactionsActivity.class));
        } else if(v.getId() == R.id.add_flight_button) {
            startActivity(new Intent(this, AddFlightActivity.class));
        } else if(v.getId() == R.id.main_menu_button) {
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
