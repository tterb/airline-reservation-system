package com.example.brettstevenson.airlineticketreservationsystem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class SearchResultActivity extends Activity {

    private static final String TAG =  "AirlineDemo";
    private CustomerList customerList;
    private FlightList flightList;
    private ReservationList reservationList;
    private TransactionList transactionList;
    private HashMap<String, Flight> flights;
    private Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "SearchResultActivity created");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Intent intent = getIntent();
        final String origin = intent.getStringExtra("origin");
        final String destination = intent.getStringExtra("destination");
        final String username = intent.getStringExtra("username");
        ListView listView = findViewById(R.id.flight_results);
        customerList = CustomerList.get(this);
        flightList = FlightList.get(this);
        reservationList = ReservationList.get(this);
        transactionList = TransactionList.get(this);
        flights = flightList.getFlights();
        customer = customerList.getCustomers().get(username);
//        customer = customers.get(username);
//        ArrayList<Flight> matches = getMatchingFlights(origin, destination);
        ArrayList<Flight> matches = new ArrayList<>();
        ArrayList<String> flightIds = new ArrayList<>();
        for(String id : flights.keySet()) {
            Flight flight = flights.get(id);
            if(origin.equals(flight.getDeparture()) && destination.equals(flight.getArrival())) {
                matches.add(flight);
                flightIds.add(id);
            }
        }
        if(matches.size() > 0) {
            Log.d(TAG, "Matches: "+matches);
            listViewAdapter adapter = new listViewAdapter(this, matches, flightIds);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            displayErrorDialog();
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String flightId = ((TextView)view.findViewById(R.id.list_id)).getText().toString();
                Log.d(TAG, flightId+" clicked!");
                displaySeatCountDialog(flights.get(flightId));
            }
        });
    }

    public void displaySeatCountDialog(Flight targetFlight) {
        final Flight flight = targetFlight;
        AlertDialog countDialog = new AlertDialog.Builder(this).create();
        countDialog.setTitle("Seat Quantity");
        countDialog.setCancelable(true);
        countDialog.setMessage("How many seats would you like to reserve?");
        final LayoutParams lp = new LayoutParams(50, 30);
        final EditText seatCountInput = new EditText(this);
        seatCountInput.setLayoutParams(lp);
        countDialog.setView(seatCountInput);
        countDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                try {
                    int count = Integer.parseInt(seatCountInput.getText().toString());
                    if(count > flight.getSeats()) {
                        dialog.dismiss();
                        Toast.makeText(SearchResultActivity.this, "There are not enough remaining seats", Toast.LENGTH_LONG).show();
                    } else if(count < 0 || count > 9) {
                        dialog.dismiss();
                        Toast.makeText(SearchResultActivity.this, "Invalid ticket count", Toast.LENGTH_SHORT).show();
                    } else {
                        displayConfirmation(flight, count);
                    }
                } catch(Exception e) {
                    dialog.dismiss();
                    Toast.makeText(SearchResultActivity.this, "Invalid ticket count", Toast.LENGTH_SHORT).show();
                }
            }
        });
        countDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        countDialog.show();
    }

    public void displayErrorDialog() {
        Log.d(TAG, "Error: No matching flights");
        AlertDialog errorMsg  = new AlertDialog.Builder(this).create();
        errorMsg.setTitle("Oops");
        errorMsg.setCancelable(false);
        errorMsg.setMessage("There are no matching flights.");
        errorMsg.setButton(AlertDialog.BUTTON_NEUTRAL, "Exit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getBaseContext(), MainActivity.class));
                dialog.dismiss();
            }
        });
        errorMsg.show();
    }

    // TODO: Second reservation is not being logged
    public void displayConfirmation(Flight target, final int seatCount) {
        final Flight flight = target;
        final double price = flight.getPrice()*seatCount;
        int resNum = 0;
        if(reservationList.getReservations().size() > 0) {
            resNum = Collections.max(reservationList.getReservations().keySet())+1;
        }
        final int reservationNum = resNum;
        AlertDialog confDialog = new AlertDialog.Builder(this).create();
        confDialog.setTitle("Confirm");
        confDialog.setCancelable(true);
        confDialog.setMessage("Customer: "+customer.getUsername()+"\nFlight number: "
                                +flight.getId()+"\nDeparture: "+flight.getDeparture()
                                +", "+flight.getTime()+"\nArrival: "+flight.getArrival()+"\nNumber of tickets: "
                                +seatCount+"\nReservation number: "+reservationNum+"\nTotal amount: "+NumberFormat.getCurrencyInstance().format(price));
        confDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "Reservation confirmed!");
                Reservation reservation = new Reservation(customer.getUsername(), flight, seatCount, price);
                reservationList.addReservation(reservation);
                transactionList.addTransaction(reservation);
                customer.addReservation(reservation);
                customerList.updateCustomer(customer);
                customerList.updateList();
                flightList.getFlight(flight.getId()).reserveSeats(seatCount);
                flightList.updateList();
                Log.d(TAG, "Reservations: "+reservationList.getReservations());
                startActivity(new Intent(getBaseContext(), MainActivity.class));
                Toast.makeText(getApplicationContext(), "Reservation Created", Toast.LENGTH_SHORT).show();
            }
        });
        confDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                confirmCancellation();
            }
        });
        confDialog.show();
    }

    public void confirmCancellation() {
        AlertDialog cancelDialog = new AlertDialog.Builder(this).create();
        cancelDialog.setTitle("Are you sure?");
        cancelDialog.setMessage("Are you sure that you want to cancel the reservation?");
        cancelDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "Reservation cancelled!");
                startActivity(new Intent(getBaseContext(), MainActivity.class));
                Toast.makeText(getApplicationContext(), "Reservation Cancelled", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        cancelDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        cancelDialog.show();
    }
}
