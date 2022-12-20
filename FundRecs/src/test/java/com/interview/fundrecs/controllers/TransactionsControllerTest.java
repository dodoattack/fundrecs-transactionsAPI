package com.interview.fundrecs.controllers;

import com.interview.fundrecs.model.Transaction;
import com.interview.fundrecs.model.TransactionsAggregate;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TransactionsControllerTest {
    //private TransactionsAggregate transactionsAggregate = new TransactionsAggregate();
    //private TransactionsController transactionsController = new TransactionsController(transactionsAggregate);
    private List<Transaction> transactions = new ArrayList<>();

    @Test
    public void submitTransaction(){
        Transaction transaction1 = new Transaction();
        transaction1.setAmount("12");
        transaction1.setDate("02-12-2021");
        transaction1.setType("credit");
        Transaction transaction2 = new Transaction();
        transaction2.setAmount("12");
        transaction2.setDate("02-12-2021");
        transaction2.setType("credit");
        transactions.add(transaction1);
        transactions.add(transaction2);

        //transactionsController.submitTransaction(transactions);
    }

}
