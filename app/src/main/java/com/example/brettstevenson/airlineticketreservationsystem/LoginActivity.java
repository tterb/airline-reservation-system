package com.example.brettstevenson.airlineticketreservationsystem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import java.util.HashMap;


public class LoginActivity extends Activity {

    private static final String TAG =  "AirlineDemo";
    private static int attemptCount = 0;
    private CustomerList customerList;
    private HashMap<String, Customer> customers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d(TAG, "login view created");
        Intent intent = getIntent();
        final String action = intent.getStringExtra("action");
        final boolean admin = intent.getBooleanExtra("admin", false);
        customerList = CustomerList.get(this);
        customerList.updateList();
        customers = customerList.getCustomers();
        Log.d(TAG, "customers retrieved");
        // Set up a click listener for the "Submit" primary_button.
        View loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText username_input = findViewById(R.id.username_input);
                EditText password_input = findViewById(R.id.password_input);
                if (v.getId() == R.id.login_button) {
                    Log.d(TAG, "loginButton clicked");
                    String username = username_input.getText().toString();
                    String password = password_input.getText().toString();
                    boolean valid = validateLogin(username, password, admin);
                    if (valid) {
                        redirectCustomer(username, action);
                    } else {
                        displayInputError();
                        username_input.setText("");
                        password_input.setText("");
                    }
                }
            }
        });
        Log.d(TAG, "loginButton listener set");
    }

    public boolean validateLogin(String username, String password, boolean admin) {
        Log.d(TAG, customers.toString());
        if(customers.get(username) != null) {
            Log.d(TAG, username+" found");
            Customer customer = customers.get(username);
            if(admin) {
                return (customer.getPassword().equals(password) && customer.isAdmin());
            } else {
                return customer.getPassword().equals(password);
            }
        }
        Log.d(TAG, username+" not found");
        return false;
    }

    public void redirectCustomer(String username, String target) {
        Log.d(TAG, "redirecting user to "+target);
        if(target.equals("reserve")) {
            Intent intent = new Intent(this, ReserveSeatActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        } else if(target.equals("cancel")) {
            Intent intent = new Intent(this, CancelReservationActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        } else if(target.equals("manage")) {
            Intent intent = new Intent(this, ManageSystemActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        }
    }

    public void displayInputError() {
        AlertDialog errorMsg  = new AlertDialog.Builder(this).create();
        errorMsg.setTitle("Oops");
        errorMsg.setCancelable(false);
        errorMsg.setMessage("The username and/or password you entered is incorrect.");
        errorMsg.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                attemptCount++;
                if(attemptCount > 2) {
                    openMainActivity();
                    dialog.dismiss();
                }
            }
        });
        errorMsg.show();
    }

    public void openMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
