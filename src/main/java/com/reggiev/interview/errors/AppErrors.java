package com.reggiev.interview.errors;

public class AppErrors extends RuntimeException
{
    public AppErrors()
    {
        super();
    }
    public AppErrors(String message)
    {
        super(message);
    }
}
