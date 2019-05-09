package com.reggiev.interview.model;

import com.reggiev.interview.errors.InsufficientBalanceException;
import com.reggiev.interview.errors.InvalidValueException;
import com.reggiev.interview.model.Account;
import com.reggiev.interview.model.MonetaryAmount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 */
public class AccountTest
{
    @Test
    @DisplayName("Debit an account successfully")
    void testSuccessfulAccountDebit() {
        String balance = "4313.12";
        Account testAccount = new Account(balance);
        MonetaryAmount balanceBeforeDebit = testAccount.getBalance();
        String debitAmountString = "20.20";
        MonetaryAmount debitAmount = new MonetaryAmount(new BigDecimal(debitAmountString));

        MonetaryAmount balanceAfterDebit = testAccount.debit(debitAmount);

        MonetaryAmount expectedAmount = balanceBeforeDebit.subtract(debitAmount);
        assertEquals(expectedAmount.toBigDecimal(), balanceAfterDebit.toBigDecimal());
        assertEquals(expectedAmount.toBigDecimal(), testAccount.getBalance().toBigDecimal());
    }



    @Test
    @DisplayName("Invalid Debit amount throws InvalidValueException")
    void testInvalidAmountToDebit() {
        Account testAccount = new Account();

        assertThrows(InvalidValueException.class, () -> testAccount.debit(null));
        assertThrows(InvalidValueException.class, () -> testAccount.debit(new MonetaryAmount(new BigDecimal("-321.22"))));
    }

    @Test
    @DisplayName("Debit of account with insufficient balance throws InsufficientBalanceException")
    void testDebitWithInsufficientFunds() {
        Account testAccount = new Account("10");

        assertThrows(InsufficientBalanceException.class, () -> testAccount.debit(new MonetaryAmount(new BigDecimal("101"))));
    }



    @Test
    @DisplayName("Credit an account successfully")
    void successfulAccountCredit() {
        Account testAccount = new Account("424.32");
        MonetaryAmount balanceBeforeCredit = testAccount.getBalance();

        MonetaryAmount amountToCredit = new MonetaryAmount(new BigDecimal("30.28"));
        MonetaryAmount balanceAfterCredit = testAccount.credit(amountToCredit);

        MonetaryAmount expectedAmount = balanceBeforeCredit.add(amountToCredit);
        assertEquals(expectedAmount.toBigDecimal(), balanceAfterCredit.toBigDecimal());
        assertEquals(expectedAmount.toBigDecimal(), testAccount.getBalance().toBigDecimal());
    }

    @Test
    @DisplayName("Invalid Credit amount throws InvalidValueException")
    void testInvalidAmountToCredit() {
        Account testAccount = new Account();

        assertThrows(InvalidValueException.class, () -> testAccount.credit(null));
        assertThrows(InvalidValueException.class, () -> testAccount.credit(new MonetaryAmount(new BigDecimal("-321.22"))));
    }

}
