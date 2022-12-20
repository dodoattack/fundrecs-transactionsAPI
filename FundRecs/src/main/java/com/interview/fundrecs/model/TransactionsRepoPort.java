package com.interview.fundrecs.model;

import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;

@Repository
public interface TransactionsRepoPort {
    void saveTransaction(List<Transaction> json);
    Transaction findTransaction(String date, String type);
    TransactionsAggregate getTransactionsAggregate();
    TransactionsAggregate loadAlreadyPersistedTransactions() throws IOException;
}
