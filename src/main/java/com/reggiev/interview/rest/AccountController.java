package com.reggiev.interview.rest;

import com.reggiev.interview.dto.AccountDTO;
import com.reggiev.interview.errors.DuplicateAccountException;
import com.reggiev.interview.errors.InvalidValueException;
import com.reggiev.interview.model.Account;
import com.reggiev.interview.repositories.AccountsRepository;
import io.javalin.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;


/**
 * This controller contains the Javalin Handlers for the account-related REST endpoints of the money transfer service.
 */
public class AccountController
{
    static Logger log = LoggerFactory.getLogger(AccountController.class);

    private static final AccountsRepository repository = AccountsRepository.getInstance();

    public static Handler fetchAllAccounts = ctx -> {
        Collection<Account> accounts = repository.getAllAccounts();
        ctx.json(accounts);
        log.info("fetched all " + accounts.size() + " account(s)");
    };

    public static Handler createAccount = ctx -> {
        AccountDTO accountDTO = ctx.bodyValidator(AccountDTO.class)
                .check(a -> a.id != null && a.balance.amount != null,
                        "Need an id and amount parameter in the json request.")
                .get();
        try
        {
            Account account;
            if (accountDTO.balance.currency != null && accountDTO.balance.currency.length() > 0)
                account = new Account(accountDTO.id, accountDTO.balance.amount, accountDTO.balance.currency);
            else
                account = new Account(accountDTO.id, accountDTO.balance.amount);
            repository.addAccount(account);
            log.info("created account " + accountDTO.id);
            ctx.json(account);
        }
        catch (DuplicateAccountException e)
        {
            ctx.result(e.getMessage());
            ctx.status(400);
        }
        catch (InvalidValueException e)
        {
            ctx.result(e.getMessage());
            ctx.status(400);
        }
        catch (Exception e)
        {
            log.error(e.getMessage());
            ctx.status(500);
        }

    };

    public static Handler fetchAccount = ctx -> {

        String id = ctx.pathParam("id");
        Account account = repository.getById(id);
        if (account == null)
        {
            ctx.result("Account with id " + id + " not found.");
            ctx.status(400);
            return;
        }
        ctx.json(account);
        log.info("fetched account " + account.getId());
    };

    public static Handler deleteAccount = ctx -> {

        String id = ctx.pathParam("id");
        boolean ok = repository.deleteAccount(id);
        if (!ok)
        {
            ctx.result("Account with id " + id + " not found.");
            ctx.status(400);
            return;
        }
        log.info("deleted account " + id);
        ctx.result("deleted account with id " + id);

    };
}
