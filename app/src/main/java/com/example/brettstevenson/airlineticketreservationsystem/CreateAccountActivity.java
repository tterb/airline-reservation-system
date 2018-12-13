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
import android.widget.Toast;
import java.util.HashMap;

public class CreateAccountActivity extends Activity implements OnClickListener {

    private static final String TAG =  "AirlineDemo";
    private CustomerList customerList;
    private TransactionList transactionList;
    private HashMap<String, Customer> customers;
    private static int attemptCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        Log.d(TAG, "create-account view created");
        customerList = CustomerList.get(this);
        customers = customerList.getCustomers();
        transactionList = TransactionList.get(this);
        View submitAccountButton = findViewById(R.id.submit_account_button);
        submitAccountButton.setOnClickListener(this);
    }

    public void onClick(View v) {
        EditText username_input = findViewById(R.id.username_input);
        EditText password_input = findViewById(R.id.password_input);
        EditText confirmation_input = findViewById(R.id.confirmation_input);
        Log.d(TAG, "clicked registered");
        if (v.getId() == R.id.submit_account_button) {
            Log.d(TAG, "submitAccountButton clicked");
            String username = username_input.getText().toString();
            String password = password_input.getText().toString();
            String passConfirm = confirmation_input.getText().toString();
            boolean valid = validateAccountInfo(username, password, passConfirm);
            Log.d(TAG, "validateAccountInfo() returned: "+valid);
            if (valid) {
                Customer newCustomer = new Customer(username, password, false);
                customerList.addCustomer(newCustomer);
                Transaction transaction = new NewAccount(newCustomer.getUsername());
                transactionList.addTransaction(transaction);
                Toast.makeText(getApplicationContext(), "Account Created", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getBaseContext(), MainActivity.class));
            } else {
                String msg;
                if(isValid(username) && isValid(password))
                    msg = "The provided username is already in use.";
                else
                    msg = "The username and/or password you entered are not in the correct format.";
                displayInputError(msg);
                username_input.setText("");
                password_input.setText("");
                confirmation_input.setText("");
            }
        }
    }

    public boolean validateAccountInfo(String username, String password, String passConfirm) {
        Log.d(TAG, "validateAccountInfo() called: ");
        Log.d(TAG, "username: "+username+"\npassword: "+password+"\npassConfirm: "+passConfirm);
        if(customers.get(username) == null && isValid(username)
                && isValid(password) && password.equals(passConfirm))
            return true;
        return false;
    }

    private boolean isValid(String str) {
        int charCount = 0;
        int numCount = 0;
        for (int i = 0; i < str.length(); i++) {
            if(Character.isLetter(str.charAt(i)))
                charCount++;
            else if(Character.isDigit(str.charAt(i)))
                numCount++;
        }
        return (charCount >= 3 && numCount >= 1);
    }

    public void displayInputError(String msg) {
        attemptCount++;
        Log.d(TAG, "Error in account info. Attempt: "+attemptCount);
        AlertDialog errorMsg  = new AlertDialog.Builder(this).create();
        errorMsg.setTitle("Oops");
        errorMsg.setCancelable(false);
        if(attemptCount < 2)
            msg += "\nPlease try again.";
        errorMsg.setMessage(msg);
        errorMsg.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if(attemptCount >= 2) {
                    startActivity(new Intent(getBaseContext(), MainActivity.class));
                    dialog.dismiss();
                }
            }
        });
        errorMsg.show();
    }
}
