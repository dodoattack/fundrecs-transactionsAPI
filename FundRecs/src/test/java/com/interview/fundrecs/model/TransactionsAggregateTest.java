package com.interview.fundrecs.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TransactionsAggregateTest {
    private TransactionsAggregate transactionsAggregate = new TransactionsAggregate();
    private List<Transaction> transactions = new ArrayList<>();

    @Before
    public void setUp(){
        Transaction transaction1 = new Transaction();
        transaction1.setAmount("1");
        transaction1.setDate("11-12-2018");
        transaction1.setType("credit");
        transactions.add(transaction1);

        Transaction transaction2 = new Transaction();
        transaction2.setAmount("2");
        transaction2.setDate("01-01-2018");
        transaction2.setType("credit");
        transactions.add(transaction2);
    }

    @Test
    public void checkThatWhenTheDateAndTypeMatch_TheAmountsAreSummedAndOneRecordIsStored(){
        Transaction transaction1 = new Transaction();
        transaction1.setAmount("3");
        //Date matches date of transaction in the 'before' method.
        transaction1.setDate("11-12-2018");
        //Type matches type of transaction in the 'before' method.
        transaction1.setType("credit");
        transactions.add(transaction1);

        transactionsAggregate.addTransactionsWithChecks(transactions);
        Transaction transaction = transactionsAggregate.getTransaction("11-12-2018", "credit");
        Assert.assertEquals("Check that there are two transactions", "4.0", transaction.getAmount());
    }

    @Test
    public void checkThatANewTypeIsStored(){
        Transaction transaction1 = new Transaction();
        transaction1.setAmount("3");
        //Date matches date of transaction in the 'before' method.
        transaction1.setDate("11-12-2018");
        //Type matches type of transaction in the 'before' method.
        transaction1.setType("debit");
        transactions.add(transaction1);

        transactionsAggregate.addTransactionsWithChecks(transactions);
        Transaction transaction = transactionsAggregate.getTransaction("11-12-2018", "debit");
        Assert.assertEquals("Check that there are two transactions", "debit", transaction.getType());
        Assert.assertEquals("Check that there are two transactions", "3", transaction.getAmount());
    }

    @Test
    public void checkTransactionAreCorrectlyAddedWithoutChecks(){
        Transaction transaction1 = new Transaction();
        transaction1.setAmount("1");
        transaction1.setDate("11-01-2020");
        transaction1.setType("debit");
        Transaction transaction2 = new Transaction();
        transaction2.setAmount("1");
        transaction2.setDate("11-01-2020");
        transaction2.setType("credit");
        transactions.add(transaction1);
        transactions.add(transaction2);

        transactionsAggregate.addTransactionsWithoutChecks(transactions);

        Map<String, List<Transaction>> transactions = transactionsAggregate.getTransactions();
        List<Transaction> transactionList = transactions.get("11-01-2020");

        Assert.assertEquals(2, transactionList.size());
        Assert.assertEquals("Check that the transactions contain the same date only", "11-01-2020", transactionList.get(0).getDate());
        Assert.assertEquals("Check that the transactions contain the same date only", "11-01-2020", transactionList.get(1).getDate());
    }
}
