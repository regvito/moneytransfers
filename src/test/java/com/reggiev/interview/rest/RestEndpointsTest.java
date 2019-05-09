package com.reggiev.interview.rest;

import com.reggiev.interview.dto.AccountDTO;
import com.reggiev.interview.dto.TransferDTO;
import com.reggiev.interview.model.Account;
import com.reggiev.interview.repositories.AccountsRepository;
import com.reggiev.interview.service.TransferService;
import io.javalin.Javalin;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.expect;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;


public class RestEndpointsTest
{
    static Logger log = LoggerFactory.getLogger(RestEndpointsTest.class);

    private static final TransferService transferService = TransferService.getInstance();
    private static final AccountsRepository repository = AccountsRepository.getInstance();
    private static final String sourceAccountId = "1234";
    private static final String targetAccountId = "4321";
    private static final int PORT = 7001;
    private static Javalin server = Javalin.create().start(PORT);

    @BeforeAll
    public static void setUp()
    {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = PORT;
        new RestEndpoints().configure(server);
    }


    @Test
    public void testSuccessfulAccountCreation() {
        log.info("running testAccountCreation");

        AccountDTO account = new AccountDTO();
        account.id = "9876";
        account.balance.amount = "533.13";


        given()
            .contentType("application/json")
            .body(account)
        .when()
            .post("/accounts")
        .then()
                .body("id", equalTo(account.id))
                .body("balance.amount", equalTo(account.balance.amount))
                .body("balance.currency", equalTo("USD"))
        .assertThat()
        .statusCode(HttpStatus.SC_OK)
        ;
    }

    @Test
    public void testAccountCreationWithInvalidCurrency() {
        log.info("running testAccountCreation");

        AccountDTO account = new AccountDTO();
        account.id = "9876";
        account.balance.amount = "533.13";
        account.balance.currency = "xxx"; // invalid currency

        given()
                .contentType("application/json")
                .body(account)
        .when()
                .post("/accounts")
        .then()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
        ;
    }

    @Test
    public void testValidAccountRetrieval() {
        dataSetup();
        expect().statusCode(200).contentType(ContentType.JSON)
                .when().get("/accounts/" + sourceAccountId);

        given().when()
                    .get("/accounts/" + sourceAccountId)
                .then()
                    .body("id", equalTo(sourceAccountId))
                    .body("balance.amount", equalTo("312.33"))
        ;
    }

    @Test
    public void testInvalidAccountRetrieval() {
        expect().statusCode(400)
                .when().get("/accounts/xxx");
    }

    @Test
    public void testRetrievalOfAllAccounts()
    {
        given()
            .when()
                .get("/accounts")
            .then()
                .body("size()", is(2));
    }

    @Test
    public void testSuccessfulTransfer()
    {
        dataSetup();
        TransferDTO transferDTO = new TransferDTO();
        transferDTO.sourceAccount = sourceAccountId;
        transferDTO.destinationAccount = targetAccountId;
        transferDTO.amount = "32.13";

        given()
            .contentType("application/json")
            .body(transferDTO)
        .when()
            .post("/transfer")
        .then()
            .assertThat()
            .statusCode(HttpStatus.SC_OK)
        ;
    }

    @Test
    public void testUnsuccessfulTransfer()
    {
        TransferDTO transferDTO = new TransferDTO();
        transferDTO.sourceAccount = sourceAccountId;
        transferDTO.destinationAccount = targetAccountId;
//        transferDTO.amount = "32.13"; // missing amount json parameter to simulate bad request

        given()
            .contentType("application/json")
            .body(transferDTO)
        .when()
            .post("/transfer")
        .then()
            .assertThat()
            .statusCode(HttpStatus.SC_BAD_REQUEST)
        ;
    }

    private void dataSetup() {
        repository.deleteAllAccounts();

        Account sourceAccount = new Account(sourceAccountId, "312.33");
        Account targetAccount = new Account(targetAccountId, "749.31");

        repository.addAccount(sourceAccount);
        repository.addAccount(targetAccount);
    }

}
