package com.example.brettstevenson.airlineticketreservationsystem;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import java.text.NumberFormat;

public class AddFlightActivity extends AppCompatActivity {

    private static final String TAG =  "AirlineDemo";
    private static int attemptCount = 0;
    private FlightList flightList;
    private CustomerList customerList;
    private TransactionList transactionList;
    private Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_flight);
        Intent intent = getIntent();
        final String username = intent.getStringExtra("username");
        flightList = FlightList.get(this);
        customerList = CustomerList.get(this);
        transactionList = TransactionList.get(this);
        customer = customerList.getCustomers().get(username);
        populateSpinners();
        Button addFlight = findViewById(R.id.add_flight_button);
        final EditText timeInput = findViewById(R.id.flight_time_input);
        timeInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddFlightActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        String amPm;
                        if (hourOfDay >= 12)
                            amPm = "PM";
                        else
                            amPm = "AM";
                        timeInput.setText(String.format("%2d:%02d", hourOfDay, minutes) + amPm);
                    }
                }, 0, 0, false);
                timePickerDialog.show();
            }
        });
        addFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptCount++;
                boolean hasError = false;
                EditText flightNumInput = findViewById(R.id.flight_num_input);
                Spinner departures = findViewById(R.id.departure_spinner);
                Spinner arrivals = findViewById(R.id.arrival_spinner);
                EditText timeInput = findViewById(R.id.flight_time_input);
                EditText capacityInput = findViewById(R.id.flight_capacity_input);
                EditText priceInput = findViewById(R.id.flight_price_input);
                String flightNum = flightNumInput.getText().toString();
                String origin = departures.getSelectedItem().toString();
                String destination = arrivals.getSelectedItem().toString();
                String time = timeInput.getText().toString();
                if(flightNum.equals("") || time.equals("") || capacityInput.getText().toString().equals("")
                        || priceInput.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "You must populate all entries", Toast.LENGTH_SHORT).show();
                    hasError = true;
                } else if(flightList.getFlight(flightNum) != null) {
                    Toast.makeText(getApplicationContext(), "Flight number is already taken", Toast.LENGTH_SHORT).show();
                    flightNumInput.setText("");
                    hasError = true;
                } else if(origin.equals(destination)) {
                    Toast.makeText(getApplicationContext(), "Flight must have a different origin and departure", Toast.LENGTH_SHORT).show();
                    hasError = true;
                } else {
                    int capacity = Integer.parseInt(capacityInput.getText().toString());
                    double price = Double.parseDouble(priceInput.getText().toString());
                    displayConfirmation(new Flight(flightNum, origin, destination, time, price, capacity));
                }
                if(hasError && attemptCount > 2) {
                    startActivity(new Intent(getBaseContext(), MainActivity.class));
                }
            }
        });
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

    public void displayConfirmation(Flight targetFlight) {
        final Flight flight = targetFlight;
        AlertDialog confDialog = new AlertDialog.Builder(this).create();
        confDialog.setTitle("Confirm");
        confDialog.setCancelable(true);
        confDialog.setMessage("Flight Number: "+flight.getId()+"\nDeparture: "+flight.getDeparture()+
                              ", "+flight.getTime()+"\nArrival: "+flight.getArrival()+
                              "\nPrice: "+NumberFormat.getCurrencyInstance().format(flight.getPrice()));
        confDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "Flight addition confirmed!");
                flightList.addFlight(flight);
//                Transaction transaction = new NewFlight(customer.getUsername(), flight);
                transactionList.addTransaction(new NewFlight(customer.getUsername(), flight));
                Intent intent = new Intent(getBaseContext(), ManageSystemActivity.class);
                startActivity(intent);
            }
        });
        confDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "Reservation cancelled!");
                dialog.dismiss();
            }
        });
        confDialog.show();
    }
}
