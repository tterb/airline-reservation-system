package com.example.brettstevenson.airlineticketreservationsystem;

public class NewAccount extends Transaction {

    public NewAccount(String customer) {
        super("New Account", customer);
        super.setDateTime();
    }
    public NewAccount() { this(null); }

    @Override
    public String toString() {
        return "Transaction type: "+super.getType()+"\nCustomer's Username: "+super.getCustomer()
                +"\nTransaction Date: "+super.getDate()+"\nTransaction Time: "+super.getTime();
    }
}
