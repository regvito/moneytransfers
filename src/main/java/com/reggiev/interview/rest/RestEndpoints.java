package com.reggiev.interview.rest;

import io.javalin.Javalin;

/**
 * This defines the REST endpoints of the money transfer service and their corresponding controllers .
 */
public class RestEndpoints
{
    static boolean configured = false;

    public void configure(Javalin app)
    {
        if (!configured)
        {
            app.get("/", ctx -> ctx.result("Transfer Service"));
            app.get("/accounts", AccountController.fetchAllAccounts);
            app.get("/accounts/:id", AccountController.fetchAccount);
            app.delete("/accounts/:id", AccountController.deleteAccount);
            app.post("/accounts", AccountController.createAccount);

            app.post("/transfer", TransferController.transfer);
            configured = true;
        }
    }
}
