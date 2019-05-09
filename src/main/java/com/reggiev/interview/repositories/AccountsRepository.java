package com.reggiev.interview.repositories;

import com.reggiev.interview.errors.DuplicateAccountException;
import com.reggiev.interview.model.Account;
import com.reggiev.interview.rest.AccountController;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Stores accounts in-memory.
 */
public class AccountsRepository
{

    private static final AccountsRepository INSTANCE = new AccountsRepository(new ConcurrentHashMap<>());
    private final Map<String, Account> accounts;


    private AccountsRepository(Map<String, Account> accounts) {
        this.accounts = accounts;
    }

    public static AccountsRepository getInstance() {
        return INSTANCE;
    }

    public Account getById(String id) {
        return id == null ? null : accounts.get(id);
    }

    public Collection<Account> getAllAccounts() {
        return accounts.values();
    }

    public Account addAccount(Account account) {
        Account accountExists = accounts.putIfAbsent(account.getId(), account);
        if (accountExists != null) {
            throw new DuplicateAccountException(accountExists.getId());
        }

        return getById(account.getId());
    }

    public boolean deleteAccount(String id)
    {
        return accounts.remove(id) != null;
    }

    public void deleteAllAccounts() {
        synchronized (accounts) {
            accounts.clear();
        }
    }
}
