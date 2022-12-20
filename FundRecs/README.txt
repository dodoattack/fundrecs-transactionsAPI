//POST request
curl -X POST -H "Content-type: application/json" -d '[{"date":"02-02-2012", "amount":"20.20", "type":"credit"}]' "http://localhost:8080/api/transaction/submit"

//GET request
curl -X GET -H "Content-type: application/json" -d '{"date":"02-02-2012", "type":"credit"}' "http://localhost:8080/api/transaction/get"


JSON records storage location:
TransactionStore/transactions.json