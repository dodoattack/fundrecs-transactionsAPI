Developer notes:

    // API - POST request sample
        curl -X POST -H "Content-type: application/json" -d '[{"date":"02-02-2012", "amount":"20.20", "type":"credit"}]' "http://localhost:8080/api/transaction/submit"

    // API - GET request sample
        curl -X GET -H "Content-type: application/json" -d '{"date":"02-02-2012", "type":"credit"}' "http://localhost:8080/api/transaction/get"

    // JSON transaction records - storage location:
        TransactionStore/transactions.json

    // Required configuration change for log4j.properties file
        - Change the following property: log4j.appender.file.File=<path>

    // Technology versions used/required
        maven - 3.8.1
        java - 1.8

    // Build the executable
        mvn clean
        mvn install
        mvn spring-boot:run

    // How to run the executable
        java -jar FundRecs-1.0-SNAPSHOT.jar <<<PATH_TO_TRANSACTION_STORE_FILE>>>
        e.g. java -jar FundRecs-1.0-SNAPSHOT.jar /Users/dodoattack/IdeaProjects/FundRecs_Interview/FundRecs/TransactionStore/transactions.json