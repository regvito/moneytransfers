package com.reggiev.interview.repositories;

import com.reggiev.interview.errors.DuplicateAccountException;
import com.reggiev.interview.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 *  Unit tests for the AccountsRepository class
 */
public class AccountsRepositoryTest
{

    private AccountsRepository repository = AccountsRepository.getInstance();

    @BeforeEach
    void clearRepository() {
        repository.deleteAllAccounts();
    }

    @Test
    @DisplayName("Retrieve account from repository")
    void testAccountRetrieval() {
        final String id = "542345432";
        Account account = new Account(id, "10");
        repository.addAccount(account);

        Account retrievedAccount = repository.getById(id);
        assertEquals(account, retrievedAccount);
    }


    @Test
    @DisplayName("Create Account Successfully With no Arguments")
    void successfulAccountCreation() {
        Account theAccount = new Account();
        repository.addAccount(theAccount);

        assertEquals(1, repository.getAllAccounts().size());
        assertEquals(theAccount, repository.getById(theAccount.getId()));
        assertEquals(BigDecimal.ZERO, repository.getById(theAccount.getId()).getBalance().toBigDecimal());
    }

    @Test
    @DisplayName("Create Account Successfully With Balance")
    void successfulAccountCreationWithBalance() {
        String balance = "1450.44";
        Account theAccount = new Account(balance);
        repository.addAccount(theAccount);

        Account retrievedAccount = repository.getById(theAccount.getId());
        assertEquals(1, repository.getAllAccounts().size());
        assertEquals(theAccount.getId(), retrievedAccount.getId());
        assertEquals(theAccount.getBalance().toBigDecimal(), retrievedAccount.getBalance().toBigDecimal());
    }

    @Test
    @DisplayName("Create Account Successfully With Balance and id")
    void successfulAccountCreationWithBalanceAndSpecifiedId() {
        String id = "84327923";
        String balance = "1450.44";
        Account theAccount = new Account(id, balance);
        repository.addAccount(theAccount);

        Account retrievedAccount = repository.getById(theAccount.getId());
        assertEquals(1, repository.getAllAccounts().size());
        assertEquals(theAccount.getId(), retrievedAccount.getId());
        assertEquals(new BigDecimal(balance), retrievedAccount.getBalance().toBigDecimal());
    }

    @Test
    @DisplayName("Adding account with existing id throws DuplicateAccountException")
    void testDuplicateAccountErrorHandling() {
        String accountId = "4321";
        Account testAccount = new Account(accountId, "500");
        repository.addAccount(testAccount);
        assertEquals(1, repository.getAllAccounts().size());

        Account duplicatedAccount = new Account(accountId, "0");
        assertThrows(DuplicateAccountException.class, () -> repository.addAccount(duplicatedAccount));
        assertEquals(1, repository.getAllAccounts().size());
    }

}
