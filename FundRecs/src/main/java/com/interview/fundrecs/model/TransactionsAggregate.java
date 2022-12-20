package com.interview.fundrecs.model;

import com.interview.fundrecs.model.validations.TransactionValidation;
import com.sun.media.sound.InvalidFormatException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TransactionsAggregate {
    private Map<String, List<Transaction>> transactions = new HashMap<>();

        public Map<String, List<Transaction>> addTransactions(List<Transaction> transactionRequests){
        transactionRequests.forEach(transaction -> {
            try {
                new TransactionValidation(transaction, this.transactions)
                        .validateDate()
                        .validateType()
                        .storeUniqueTransactionOrSumAmountAndOverwrite()
                        .getPersistedTransactions();
            } catch (InvalidFormatException e) {
                throw new RuntimeException(e.getMessage());
            }
        });
        return this.transactions;
    }

    public Transaction getTransaction(String date, String type){
        if(this.transactions.containsKey(date)) {
            return this.transactions
                    .get(date)
                    .stream()
                    .filter(transaction -> (transaction.getType().equals(type)))
                    .collect(Collectors.toList()).get(0);
        }
        return null;
    }

    public int getTransactionsCount(){
            return this.transactions.size();
    }
}
