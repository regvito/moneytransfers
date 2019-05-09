package com.reggiev.interview.dto;

/**
 * used to transfer data from json requests to the RESTful web service
 */
public class AccountDTO
{
    public String id;
    public Amount balance = new Amount();

    public class Amount
    {
        public String amount;
        public String currency;
    }

}
