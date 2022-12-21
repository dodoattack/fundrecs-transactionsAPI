package com.interview.fundrecs.model;

import com.interview.fundrecs.model.validations.TransactionValidation;
import com.sun.media.sound.InvalidFormatException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TransactionsAggregate {
    private Map<String, List<Transaction>> transactions = new HashMap<>();

    public void addTransactionsWithChecks(List<Transaction> transactionRequests) {
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
    }

    public Transaction getTransaction(String date, String type) {
        if (this.transactions.containsKey(date)) {
            return this.transactions
                    .get(date)
                    .stream()
                    .filter(transaction -> (transaction.getType().equals(type)))
                    .collect(Collectors.toList()).get(0);
        }
        return null;
    }

    public void addTransactionsWithoutChecks(List<Transaction> transactions) {
        if (this.transactions.isEmpty()) {
            transactions.forEach(transaction -> {
                if(this.transactions.containsKey(transaction.getDate())) {
                    List<Transaction> transactionList = this.transactions.get(transaction.getDate());
                    transactionList.add(transaction);
                } else {
                    List<Transaction> transactionList = new ArrayList<>();
                    transactionList.add(transaction);
                    this.transactions.put(transaction.getDate(), transactionList);
                }
            });
        }
    }

    public int getTransactionsCount() {
        return this.transactions.size();
    }

    public Map<String, List<Transaction>> getTransactions(){
        return this.transactions;
    }
}
