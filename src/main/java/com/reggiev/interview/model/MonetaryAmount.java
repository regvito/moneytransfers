package com.reggiev.interview.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.reggiev.interview.errors.InvalidValueException;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * Encapsulates monetary amounts and their currency.
 */
public class MonetaryAmount
{
    private final BigDecimal amount;
    private Currency currency = Currency.getInstance("USD"); // default

    public MonetaryAmount(BigDecimal amount)
    {
        this.amount = amount;
    }

    public MonetaryAmount(BigDecimal amount, Currency currency)
    {
        this.amount = amount;
        this.currency = currency;
    }

    public MonetaryAmount subtract(MonetaryAmount value)
    {
        if (!isSameCurrency(value))
            throw new InvalidValueException("Can only transfer money to accounts with the same currency.");

        BigDecimal result = this.amount.subtract(value.toBigDecimal());
        return new MonetaryAmount(result);
    }

    public MonetaryAmount add(MonetaryAmount value)
    {
        if (!isSameCurrency(value))
            throw new InvalidValueException("Can only add the same currency.");

        BigDecimal result = this.amount.add(value.toBigDecimal());
        return new MonetaryAmount(result);
    }

    @JsonGetter("amount") @JsonSerialize(using = ToStringSerializer.class)
    public BigDecimal toBigDecimal()
    {
        return this.amount;
    }

    public Currency getCurrency()
    {
        return this.currency;
    }

    public boolean isSameCurrency(MonetaryAmount amount)
    {
        return this.currency.getCurrencyCode().equals(amount.currency.getCurrencyCode());
    }
}
