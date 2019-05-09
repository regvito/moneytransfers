package com.reggiev.interview.errors;

public class DuplicateAccountException extends AppErrors
{
    public DuplicateAccountException(String accountId) {
        super("Duplicates are not allowed. Account with id " + accountId + " already exists. ");
    }

    public DuplicateAccountException() {
        super();
    }

}
