package com.reggiev.interview.errors;

public class InsufficientBalanceException extends AppErrors
{
    public InsufficientBalanceException() {
        super("Account balance is insufficient for transfer.");
    }

    public InsufficientBalanceException(String message) {
        super(message);
    }

}
