package com.reggiev.interview.service;

import com.reggiev.interview.errors.InsufficientBalanceException;
import com.reggiev.interview.errors.InvalidValueException;
import com.reggiev.interview.model.Account;
import com.reggiev.interview.model.MonetaryAmount;
import com.reggiev.interview.repositories.AccountsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TransferServiceTest
{
    private static final TransferService transferService = TransferService.getInstance();
    private final AccountsRepository repository = AccountsRepository.getInstance();
    private static final String sourceAccountId = "1234";
    private static final String targetAccountId = "4321";

    @BeforeEach
    void setUp() {
        repository.deleteAllAccounts();

        Account sourceAccount = new Account(sourceAccountId, "312.33");
        Account targetAccount = new Account(targetAccountId, "749.31");

        repository.addAccount(sourceAccount);
        repository.addAccount(targetAccount);
    }

    @Test
    @DisplayName("Transfer between accounts successfully")
    void testSuccesfulTransfer()
    {
        MonetaryAmount initialSourceBalance = repository.getById(sourceAccountId).getBalance();
        MonetaryAmount initialTargetBalance = repository.getById(targetAccountId).getBalance();
        MonetaryAmount amountToTransfer = new MonetaryAmount(new BigDecimal("101.20"));
        transferService.transfer(sourceAccountId, targetAccountId, amountToTransfer.toBigDecimal().toString());

        MonetaryAmount expectedAccount1Balance = initialSourceBalance.subtract(amountToTransfer);
        MonetaryAmount expectedAccount2Balance = initialTargetBalance.add(amountToTransfer);
        assertEquals(repository.getById(sourceAccountId).getBalance().toBigDecimal(), expectedAccount1Balance.toBigDecimal());
        assertEquals(repository.getById(targetAccountId).getBalance().toBigDecimal(), expectedAccount2Balance.toBigDecimal());
    }

    @Test
    @DisplayName("Invalid Source/Target accounts throw InvalidValueException")
    void testInvalidAccounts() {

        assertThrows(InvalidValueException.class, () -> transferService.transfer(null, "1234", "100.00"));
        assertThrows(InvalidValueException.class, () -> transferService.transfer("1234", null, "100.00"));
        assertThrows(InvalidValueException.class, () -> transferService.transfer("4321", "1234", "100.00"));
    }

    @Test
    @DisplayName("Cannot process accounts with different currencies")
    void testAccountsWithDifferentCurrencies() {
        String gbpAccountId = "9876";
        Account gbpAccount = new Account(gbpAccountId, "312.33", Currency.getInstance("GBP").getCurrencyCode());
        repository.addAccount(gbpAccount);

        assertThrows(InvalidValueException.class, () -> transferService.transfer(sourceAccountId, gbpAccountId, "100.00"));

    }

    @Test
    @DisplayName("Test accounts with insufficient funds")
    void testTransferWithInsufficientFunds()
    {
        MonetaryAmount amountToTransfer = new MonetaryAmount(new BigDecimal("5234.20"));
        assertThrows(InsufficientBalanceException.class, () ->
                transferService.transfer(sourceAccountId, targetAccountId, amountToTransfer.toBigDecimal().toString()));
    }

    @Test
    @DisplayName("Check correction operation with concurrent accesses to service.")
    void testConcurrentTransfers() throws InterruptedException
    {
        ExecutorService executorService = Executors.newCachedThreadPool();

        MonetaryAmount initialSumTotal = new MonetaryAmount(BigDecimal.ZERO);
        for (Account a : repository.getAllAccounts())
            initialSumTotal = initialSumTotal.add(a.getBalance());

        Runnable transferTo = () -> {
            transferService.transfer(sourceAccountId, targetAccountId, "11.91");
        };
        Runnable transferFrom = () -> {
            transferService.transfer(targetAccountId, sourceAccountId, "22.14");
        };
        for (int i = 0; i < 100; i++)
        {
            executorService.submit(transferTo);
            executorService.submit(transferFrom);
        }
        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);

        MonetaryAmount sumTotal = new MonetaryAmount(BigDecimal.ZERO);
        for (Account a : repository.getAllAccounts())
            sumTotal = sumTotal.add(a.getBalance());


        // total balances of all accounts should be the same before and after all of the money transfers
        assertEquals(initialSumTotal.toBigDecimal(), sumTotal.toBigDecimal());

    }
}
