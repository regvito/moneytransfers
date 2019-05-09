package com.reggiev.interview.rest;

import com.reggiev.interview.dto.TransferDTO;
import com.reggiev.interview.errors.AppErrors;
import com.reggiev.interview.service.TransferService;
import io.javalin.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This controller contains the Javalin Handler for the REST endpoint of the money transfer service.
 */
public class TransferController
{
    static Logger log = LoggerFactory.getLogger(TransferController.class);

    private static final TransferService transferService = TransferService.getInstance();


    /**
     * Validates the moeny transfer input and calls the transfer service.
     */
    public static Handler transfer = ctx -> {
        TransferDTO transfer = ctx.bodyValidator(TransferDTO.class)
                .check(a -> a.sourceAccount != null && a.destinationAccount != null && a.amount != null,
                        "Need sourceAccount/destinationAccount and amount parameter in the json request.")
                .get();
        try
        {
            transferService.transfer(transfer.sourceAccount, transfer.destinationAccount, transfer.amount);
            String message = "transferred " + transfer.amount + " from account " +
                    transfer.sourceAccount + " to account " + transfer.destinationAccount;
            ctx.result(message);
            log.info(message);
        }
        catch (AppErrors ae)
        {
            log.error(ae.getMessage());
            ctx.result(ae.getMessage());
            ctx.status(400);
        }


    };


}
