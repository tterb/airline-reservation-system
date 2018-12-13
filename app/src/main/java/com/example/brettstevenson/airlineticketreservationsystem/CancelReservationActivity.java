package com.example.brettstevenson.airlineticketreservationsystem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;

public class CancelReservationActivity extends AppCompatActivity {

    private static final String TAG =  "AirlineDemo";
    private Customer customer;
    private CustomerList customerList;
    private FlightList flightList;
    private ReservationList reservationList;
    private TransactionList transactionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "CancelReservationActivity created");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_reservation);
        Intent intent = getIntent();
        final String username = intent.getStringExtra("username");
        customerList = CustomerList.get(this);
        flightList = FlightList.get(this);
        reservationList = ReservationList.get(this);
        transactionList = TransactionList.get(this);
        customer = customerList.getCustomers().get(username);
        ListView listView = findViewById(R.id.reservation_list);
        final HashMap<Integer, Reservation> reservations = reservationList.getReservations();
        final HashMap<Integer, Reservation> customerRes = new HashMap<>();
        for(int id : reservations.keySet()) {
            if(reservations.get(id).getCustomer().equals(customer.getUsername())) {
                customerRes.put(id, reservations.get(id));
            }
        }
//        final HashMap<Integer, Reservation> reservations = customer.getReservations();
        if(customerRes.size() > 0) {
            Log.d(TAG, "Reservations: "+customerRes);
            ArrayList<Flight> flights = new ArrayList<>();
            ArrayList<String> reservationIds = new ArrayList<>();
            ArrayList<Integer> seatCounts = new ArrayList<>();
            for(int id : customerRes.keySet()) {
                flights.add(customerRes.get(id).getFlight());
                reservationIds.add(String.valueOf(id));
                seatCounts.add(customerRes.get(id).getTicketCount());
            }
            listViewAdapter adapter = new listViewAdapter(this, flights, reservationIds, seatCounts);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            displayErrorDialog();
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int reservationId = Integer.parseInt(((TextView) view.findViewById(R.id.hidden_id)).getText().toString());
                Log.d(TAG, reservationId+" clicked!");
                displayConfirmation(reservations.get(reservationId));
            }
        });
    }

    public void displayErrorDialog() {
        Log.d(TAG, "Error: No reservations for username");
        AlertDialog errorMsg  = new AlertDialog.Builder(this).create();
        errorMsg.setTitle("Oops");
        errorMsg.setCancelable(false);
        errorMsg.setMessage("There are no reservations for this username.");
        errorMsg.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getBaseContext(), MainActivity.class));
                dialog.dismiss();
            }
        });
        errorMsg.show();
    }

    // TODO: Should display reservation price
    public void displayConfirmation(Reservation cancelReservation) {
        final Reservation reservation = cancelReservation;
        final Reservation cancellation = new Reservation(reservation);
        AlertDialog confDialog = new AlertDialog.Builder(this).create();
        confDialog.setTitle("Confirm");
        confDialog.setCancelable(true);
        confDialog.setMessage(cancellation.toString());
        confDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "Cancellation confirmed!");
                reservation.cancel();
                transactionList.addTransaction(cancellation);
                customer.cancelReservation(reservation);
                reservationList.deleteReservation(reservation);
                Flight flight = reservation.getFlight();
                flightList.getFlight(flight.getId()).addSeats(reservation.getTicketCount());
                flightList.updateList();
                startActivity(new Intent(getBaseContext(), MainActivity.class));
                Toast.makeText(getApplicationContext(), "Reservation Cancelled", Toast.LENGTH_SHORT).show();
            }
        });
        confDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "Cancellation cancelled");
                startActivity(new Intent(getBaseContext(), MainActivity.class));
            }
        });
        confDialog.show();
    }
}
