package com.reggiev.interview.errors;


public class InvalidValueException extends AppErrors
{
    public InvalidValueException() {
        super("Accounts are invalid.");
    }

    public InvalidValueException(String message) {
        super(message);
    }

}
