
This implements a RESTful web service that enables money transfers between accounts.


## API


### Money Transfers


- POST [http://localhost:7000/transfer](http://localhost:7000/transfer)

Sample request
```javascript
{
  "sourceAccount" : "1234",
  "destinationAccount" : "4321",
  "amount" : "214.76"
}
```
```javascript
Response Status Code: 200 OK
```
NOTE: This can only transfer amounts between accounts with the same currency.

__**List of Possible Transfer Errors**__

**Response Code 400 BAD REQUEST**
```javascript
Account balance is insufficient for transfer.
```
```javascript
Accounts are not valid
```
```javascript
Accounts cannot have different currencies.
```

### Accounts


__Add acount__

- POST [http://localhost:7000/accounts](http://localhost:7000/accounts)

Sample request (defaults to currency of USD)
```javascript
{
  "id" : "1234",
  "balance" : {
    "amount" : "214.76"
  }
}
```

Sample request with specified currency
```javascript

{
  "id" : "1234",
  "balance" : {
    "amount" : "214.76",
    "currency" : "GBP"
  }
}
```
```javascript
Response Status Code: 200 OK
```

__Retrieve all accounts__
- GET [http://localhost:7000/accounts](http://localhost:7000/accounts)

sample response

```javascript
[
{
  "id" : "1234",
  "balance" : {
    "currency" : "GBP",
    "amount" : "312.42"
  }
}, {
  "id" : "4321",
  "balance" : {
    "currency" : "USD",
    "amount" : "312.42"
  }
}
]
```
```javascript
Response Status Code 200 OK
```

__Retrieve specific account__

- GET [http://localhost:7000/accounts/1234](http://localhost:7000/accounts/1234)

sample response
```javascript
{
  "id" : "4321",
  "balance" : {
    "currency" : "USD",
    "amount" : "312.42"
  }
}
```
```javascript
Response Status Code: 200 OK
```

__**List of Possible Account Errors**__

**Status Code 400 BAD REQUEST**

```javascript
Account with id xxxx not found.
```
```javascript
Duplicates are not allowed. Account with id xxxx already exists. "
```
```javascript
Invalid currency.
```

- DELETE [http://localhost:7000/accounts/1234](http://localhost:9999/accounts/1234)


## Technology stack
- Java 8
- [Maven 3](https://maven.apache.org/)
- [Javalin](https://javalin.io/) (REST framework)
- [JUnit 5](https://junit.org/junit5/) (Testing)
- [REST-assured](http://rest-assured.io/) (Testing REST services)

## How to compile
- mvn clean install

## How to run
Without arguments, the web service starts in port 7000
```javascript
java -jar ".\target\moneytransfers-1.0-SNAPSHOT-onejar.jar"
````
To run in a specific port,
```javascript
java -jar ".\target\moneytransfers-1.0-SNAPSHOT-onejar.jar" <port>
````

## How to run unit tests
- mvn test

