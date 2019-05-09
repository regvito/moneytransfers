package com.reggiev.interview.model;

import com.reggiev.interview.errors.InvalidValueException;
import com.reggiev.interview.errors.InsufficientBalanceException;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;
import java.util.UUID;

public class Account
{

    private final String id;
    private MonetaryAmount balance;

    public Account()
    {
        this.id = UUID.randomUUID().toString();
        this.balance = new MonetaryAmount(BigDecimal.ZERO);
    }

    public Account(String balance)
    {
        this.id = UUID.randomUUID().toString();
        this.balance = new MonetaryAmount(new BigDecimal(balance));
    }

    public Account(String id, String balance)
    {
        this.id = id;
        this.balance = new MonetaryAmount(new BigDecimal(balance));
    }

    public Account(String id, String balance, String currency)
    {
        try
        {
            this.id = id;
            this.balance = new MonetaryAmount(new BigDecimal(balance), Currency.getInstance(currency));
        }
        catch (IllegalArgumentException iae)
        {
            throw new InvalidValueException("Invalid currency.");
        }
    }

    public String getId()
    {
        return id;
    }

    public MonetaryAmount getBalance()
    {
        return balance;
    }
    public void setBalance(String amount)
    {
        this.balance = new MonetaryAmount(new BigDecimal(amount));
    }

    public MonetaryAmount credit(MonetaryAmount amount)
    {
        validate(amount);

        balance = new MonetaryAmount(balance.toBigDecimal().add(amount.toBigDecimal()), balance.getCurrency());
        return balance;
    }

    // TODO: refactor exception and text
    public MonetaryAmount debit(MonetaryAmount amount)
    {
        validate(amount);

        if (balance.toBigDecimal().compareTo(amount.toBigDecimal()) < 0)
        {
            throw new InsufficientBalanceException("Debit can't be performed due to lack of funds on the account.");
        }

//        balance = new MonetaryAmount(balance.toBigDecimal().subtract(amount.toBigDecimal()), balance.getCurrency());
        this.balance = balance.subtract(amount);
        return this.balance;
    }

    // TODO: refactor exception and text

    /**
     *
     * @param amount
     */
    private void validate(MonetaryAmount amount)
    {
        if (Objects.isNull(amount) || amount.toBigDecimal().compareTo(BigDecimal.ZERO) < 0)
        {
            throw new InvalidValueException("amount needs to be positive.");
        }
    }

    public boolean isSameCurrencyAs(Account anotherAccount)
    {
        return this.getBalance().isSameCurrency(anotherAccount.balance);
    }

//    @Override
//    public String toString()
//    {
//        return "Account{" +
//                "id=" + id +
//                ", balance=" + balance +
//                '}';
//    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id.equals(account.id);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id);
    }

}
