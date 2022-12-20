// POST request sample
    curl -X POST -H "Content-type: application/json" -d '[{"date":"02-02-2012", "amount":"20.20", "type":"credit"}]' "http://localhost:8080/api/transaction/submit"

// GET request sample
    curl -X GET -H "Content-type: application/json" -d '{"date":"02-02-2012", "type":"credit"}' "http://localhost:8080/api/transaction/get"

// JSON records storage location:
    TransactionStore/transactions.json

// Required configuration change for log4j.properties file
    - Change the following property: log4j.appender.file.File=<path>
