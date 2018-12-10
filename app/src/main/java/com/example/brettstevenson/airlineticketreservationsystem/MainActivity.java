package com.example.brettstevenson.airlineticketreservationsystem;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import java.util.HashMap;

public class MainActivity extends Activity implements OnClickListener {

    private static final String TAG =  "AirlineDemo";
    private CustomerList customerList;
    private FlightList flightList;
    private HashMap<String, Customer> customers;
    private HashMap<String, Flight> flights;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        customerList = CustomerList.get(this);
        customerList.updateList();
        customers = customerList.getCustomers();

        flightList = FlightList.get(this);
        flightList.updateList();
        flights = flightList.getFlights();

        // Set up a click listener for the "Create Account" primary_button.
        View createAccountButton = findViewById(R.id.create_account_button);
        createAccountButton.setOnClickListener(this);
        // Set up a click listener for the "Reserve Seat" primary_button.
        View reserveSeatButton = findViewById(R.id.reserve_seat_button);
        reserveSeatButton.setOnClickListener(this);
        // Set up a click listener for the "Cancel Reservation" primary_button.
        View cancelReservationButton = findViewById(R.id.cancel_seat_button);
        cancelReservationButton.setOnClickListener(this);
        // Set up a click listener for the "Manage System" primary_button.
        View manageSystemButton = findViewById(R.id.manage_system_button);
        manageSystemButton.setOnClickListener(this);
    }

    public void onClick(View v) {
        if(v.getId() == R.id.create_account_button) {
            Log.d(TAG, "createAccountButton clicked");
            Intent intent = new Intent(this, CreateAccountActivity.class);
            startActivity(intent);
        } else if(v.getId() == R.id.reserve_seat_button) {
            Log.d(TAG, "reserveSeatButton clicked");
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("action", "reserve");
            intent.putExtra("admin", false);
            startActivity(intent);
        } else if(v.getId() == R.id.cancel_seat_button) {
            Log.d(TAG, "cancelReservationButton clicked");
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("action", "cancel");
            intent.putExtra("admin", false);
            startActivity(intent);
        } else if(v.getId() == R.id.manage_system_button) {
            Log.d(TAG, "manageSystemButton clicked");
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("action", "manage");
            intent.putExtra("admin", true);
            startActivity(intent);
        }
    }
}
