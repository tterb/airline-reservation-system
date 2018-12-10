package com.example.brettstevenson.airlineticketreservationsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class ReserveSeatActivity extends Activity implements OnClickListener {

    private static final String TAG =  "AirlineDemo";
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_seat);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        populateSpinners();
        // Set up a click listener for the "Search" primary_button.
        View searchSeatButton = findViewById(R.id.search_seat_button);
        searchSeatButton.setOnClickListener(this);
    }

    public void onClick(View v) {
        Spinner departures = findViewById(R.id.departure_spinner);
        Spinner arrivals = findViewById(R.id.arrival_spinner);
        if (v.getId() == R.id.search_seat_button) {
            String origin = departures.getSelectedItem().toString();
            String destination = arrivals.getSelectedItem().toString();
            Log.d(TAG, "The origin is "+origin);
            Log.d(TAG, "The destination is "+destination);
            openResultActivity(v, origin, destination);
        }
    }

    public void populateSpinners() {
        // Populate departures spinner
        Spinner departures = findViewById(R.id.departure_spinner);
        ArrayAdapter<CharSequence> adapterA = ArrayAdapter.createFromResource(this,
                R.array.departure_array, android.R.layout.simple_spinner_item);
        adapterA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departures.setAdapter(adapterA);
        // Populate arrivals spinner
        Spinner arrivals = findViewById(R.id.arrival_spinner);
        ArrayAdapter<CharSequence> adapterB = ArrayAdapter.createFromResource(this,
                R.array.arrival_array, android.R.layout.simple_spinner_item);
        adapterB.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arrivals.setAdapter(adapterB);
    }

    public void openResultActivity(View v, String origin, String destination) {
        Intent intent = new Intent(this, SearchResultActivity.class);
        intent.putExtra("origin", origin);
        intent.putExtra("destination", destination);
        intent.putExtra("username", username);
        startActivity(intent);
    }
}
