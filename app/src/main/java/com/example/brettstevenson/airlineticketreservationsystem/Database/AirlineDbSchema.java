package com.example.brettstevenson.airlineticketreservationsystem.Database;

public class AirlineDbSchema {
    /* This class only exists to define the String constants
        needed to describe the table                           */
    public static final class CustomerTable {
        public static final String NAME = "CUSTOMERS";
        public static final class Cols {
            public static final String UUID         = "uuid";
            public static final String USERNAME     = "username";
            public static final String PASSWORD     = "password";
            public static final String ADMIN        = "admin";
            public static final String RESERVATIONS = "reservations";
        }
    }

    public static final class FlightTable {
        public static final String NAME = "FLIGHTS";
        public static final class Cols {
            public static final String ID         = "id";
            public static final String DEPARTURE  = "departure";
            public static final String ARRIVAL    = "arrival";
            public static final String TIME       = "time";
            public static final String PRICE      = "price";
            public static final String CAPACITY   = "capacity";
        }
    }

    public static final class ReservationTable {
        public static final String NAME = "RESERVATIONS";
        public static final class Cols {
            public static final String ID         = "id";
            public static final String CUSTOMER   = "customer";
            public static final String FLIGHT     = "flight";
            public static final String TICKETS    = "tickets";
            public static final String PRICE      = "price";
        }
    }

    public static final class TransactionTable {
        public static final String NAME = "TRANSACTIONS";
        public static final class Cols {
            public static final String ID         = "id";
//            public static final String TYPE       = "type";
//            public static final String USERNAME   = "username";
            public static final String CONTENTS   = "contents";
        }
    }
}
