package com.interview.fundrecs.model.validations;

import com.interview.fundrecs.model.Transaction;
import com.sun.media.sound.InvalidFormatException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionValidationTest {
    private Map<String, List<Transaction>> persistedTransactions;
    private List<Transaction> transactionList;
    @Before
    public void setUp(){
        this.persistedTransactions = new HashMap<>();
        Transaction persistedTransaction1 = new Transaction();
        persistedTransaction1.setType("credit");
        persistedTransaction1.setDate("11-12-2018");
        persistedTransaction1.setAmount("9898.36");

        transactionList = new ArrayList<>();
        transactionList.add(persistedTransaction1);
        persistedTransactions.put("11-12-2018", transactionList);
    }

    @Test
    public void checkDateFormatIsValidated() throws InvalidFormatException {
        Transaction transactionRequest = new Transaction();
        transactionRequest.setType("credit");
        transactionRequest.setDate("01-02-2018");
        transactionRequest.setAmount("9898.36");

        TransactionApprovalSteps transactionApprovalSteps = new TransactionValidation(transactionRequest, persistedTransactions);
        transactionApprovalSteps.validateDate();
        Assert.assertEquals("Check date passes validation step", "01-02-2018",
                transactionApprovalSteps.getRequestedTransaction().getDate());
    }

    @Test(expected = InvalidFormatException.class)
    public void checkExceptionIsThrownWhenTheDateFormatIsIncorrect() throws InvalidFormatException {
        Transaction transactionRequest = new Transaction();
        transactionRequest.setType("credit");
        transactionRequest.setDate("2018-01-02");
        transactionRequest.setAmount("9898.36");

        TransactionApprovalSteps transactionApprovalSteps = new TransactionValidation(transactionRequest, persistedTransactions);
        transactionApprovalSteps.validateDate();
    }

    @Test
    public void checkUniqueTransactionIsStored_OrSumOfAmountOfTwoEqualTransactionsIsSummed_andThenOneIsStored() {
        Transaction transactionRequest = new Transaction();
        transactionRequest.setType("credit");
        transactionRequest.setDate("11-12-2018");
        transactionRequest.setAmount("9898.36");

        TransactionApprovalSteps transactionApprovalSteps = new TransactionValidation(transactionRequest, persistedTransactions);
        transactionApprovalSteps.storeUniqueTransactionOrSumAmountAndOverwrite();
        Assert.assertEquals("Check that only one transaction was stored however, the amount between the already" +
                " persisted transaction and the requested transaction, are summed first", "19796.72",
                transactionApprovalSteps.getPersistedTransactions().get("11-12-2018").get(0).getAmount());
    }

    @Test
    public void checkThatANewTypeIsStoredWhenTheDateValueAlreadyExists(){
        Transaction persistedTransaction = new Transaction();
        persistedTransaction.setType("credit");
        persistedTransaction.setDate("11-12-2019");
        persistedTransaction.setAmount("9898.36");
        transactionList.add(persistedTransaction);
        persistedTransactions.put("11-12-2019", transactionList);

        Transaction transactionRequest = new Transaction();
        transactionRequest.setType("debt");
        transactionRequest.setDate("11-12-2019");
        transactionRequest.setAmount("9898.36");

        TransactionApprovalSteps transactionApprovalSteps = new TransactionValidation(transactionRequest, persistedTransactions);
        transactionApprovalSteps.storeUniqueTransactionOrSumAmountAndOverwrite();

        boolean transactionWasStored = transactionApprovalSteps.getPersistedTransactions().get("11-12-2019")
                .stream()
                .filter(transaction -> transaction.getType().equals("debt"))
                .findFirst().get().getType().equals("debt");

        Assert.assertTrue("Check that a new type is stored", transactionWasStored);
    }

    @Test
    public void checkThatATransactionWithANewDateValueIsStored(){
        Transaction transactionRequest = new Transaction();
        transactionRequest.setType("credit");
        transactionRequest.setDate("11-12-2019");
        transactionRequest.setAmount("9898.36");

        TransactionApprovalSteps transactionApprovalSteps = new TransactionValidation(transactionRequest, persistedTransactions);
        transactionApprovalSteps.storeUniqueTransactionOrSumAmountAndOverwrite();
        Assert.assertEquals("Check that only one transaction was stored however, the amount between the already" +
                        "persisted transcation and the requested transaction, are summed first", "9898.36",
                transactionApprovalSteps.getPersistedTransactions().get("11-12-2019").get(0).getAmount());
    }
}
