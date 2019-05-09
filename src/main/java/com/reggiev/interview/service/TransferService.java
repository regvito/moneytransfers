package com.reggiev.interview.service;

import com.reggiev.interview.errors.InsufficientBalanceException;
import com.reggiev.interview.errors.InvalidValueException;
import com.reggiev.interview.model.Account;
import com.reggiev.interview.model.MonetaryAmount;
import com.reggiev.interview.repositories.AccountsRepository;

import java.math.BigDecimal;

/**
 *
 */
public class TransferService
{

    private final AccountsRepository repository;
    private static final TransferService INSTANCE = new TransferService(AccountsRepository.getInstance());


    private TransferService(AccountsRepository repo)
    {
        this.repository = repo;
    }

    public static TransferService getInstance() {
        return INSTANCE;
    }

    /**
     * This will transfer amount from source account to destination account
     *
     * @param sourceAccountId - id of source account
     * @param destinationAccountId - id of destination account
     * @param amount - value to transfer
     */
    public void transfer(String sourceAccountId, String destinationAccountId, String amount)
    {
        Account sourceAccount = repository.getById(sourceAccountId);
        Account destinationAccount = repository.getById(destinationAccountId);
        if (sourceAccount == null || destinationAccount == null)
            throw new InvalidValueException("Accounts are not valid.");
        if (!sourceAccount.isSameCurrencyAs(destinationAccount))
            throw new InvalidValueException("Accounts cannot have different currencies.");

        MonetaryAmount amountToTransfer = new MonetaryAmount(new BigDecimal(amount));
        transfer(sourceAccount, destinationAccount, amountToTransfer);
    }

    /**
     * Synchronized method to do the credit and debit to ensure correct operation when called by multiple systems.
     * @param sourceAccount
     * @param destinationAccount
     * @param amountToTransfer
     */
    synchronized private void transfer(Account sourceAccount, Account destinationAccount, MonetaryAmount amountToTransfer)
    {
        if (sourceAccount.getBalance().toBigDecimal().compareTo(amountToTransfer.toBigDecimal()) < 0)
            throw new InsufficientBalanceException();

        sourceAccount.debit(amountToTransfer);
        destinationAccount.credit(amountToTransfer);
    }
}
