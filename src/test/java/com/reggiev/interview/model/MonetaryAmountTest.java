package com.reggiev.interview.model;

import com.reggiev.interview.model.Account;
import com.reggiev.interview.model.MonetaryAmount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class MonetaryAmountTest
{
    @Test
    @DisplayName("Create a MonetaryAmount successfully")
    void testCreationOfMonetaryAmount() {
        BigDecimal bd = new BigDecimal("4313.12");
        MonetaryAmount amount = new MonetaryAmount(bd);

        assertEquals(amount.toBigDecimal(), bd);
    }

    @Test
    @DisplayName("Add MonetaryAmount successfully")
    void testAdditionOfMonetaryAmount() {
        BigDecimal bd = new BigDecimal("4313.12");
        MonetaryAmount amount = new MonetaryAmount(bd);
        BigDecimal bd2 = new BigDecimal("3291.10");
        MonetaryAmount amount2 = new MonetaryAmount(bd2);
        MonetaryAmount sum = amount.add(amount2);

        assertEquals(sum.toBigDecimal(), bd.add(bd2));
    }

    @Test
    @DisplayName("Add MonetaryAmount successfully")
    void testSubtractionOfMonetaryAmount() {
        BigDecimal bd = new BigDecimal("4313.12");
        MonetaryAmount amount = new MonetaryAmount(bd);
        BigDecimal bd2 = new BigDecimal("3291.10");
        MonetaryAmount amount2 = new MonetaryAmount(bd2);
        MonetaryAmount sum = amount.subtract(amount2);

        assertEquals(sum.toBigDecimal(), bd.subtract(bd2));
    }

    @Test
    @DisplayName("Check MonetaryAmount currencies")
    void testDifferentCurrencies() {
        BigDecimal bd = new BigDecimal("4313.12"); // default USD currency
        MonetaryAmount amount = new MonetaryAmount(bd);
        BigDecimal bd2 = new BigDecimal("3291.10");
        MonetaryAmount amount2 = new MonetaryAmount(bd2, Currency.getInstance("GBP"));

        assertFalse(amount.isSameCurrency(amount2));
    }

}
