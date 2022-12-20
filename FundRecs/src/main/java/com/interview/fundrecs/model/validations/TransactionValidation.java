package com.interview.fundrecs.model.validations;

import com.interview.fundrecs.model.Transaction;
import com.sun.media.sound.InvalidFormatException;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class TransactionValidation implements TransactionApprovalSteps {
    private Transaction transaction;
    private Map<String, List<Transaction>> persistedTransactions;
    final static Logger logger = Logger.getLogger("TransactionValidation");

    public TransactionValidation(Transaction transaction, Map<String, List<Transaction>> persistedTransactions){
        this.transaction = transaction;
        this.persistedTransactions = persistedTransactions;
    }

    public TransactionApprovalSteps validateDate() throws InvalidFormatException {
        //Basic date validation check for now
        if (!transaction.getDate().matches("\\d{2}-\\d{2}-\\d{4}")) {
            throw new InvalidFormatException();
        }
        return this;
    }

    public TransactionApprovalSteps validateType(){
        //Once there is a criteria defined for type we can perform a validation check for it here
        return this;
    }

    public TransactionApprovalSteps storeUniqueTransactionOrSumAmountAndOverwrite(){
        if(persistedTransactions.containsKey(transaction.getDate())) {
            List<Transaction> sameDatedTransactions = persistedTransactions.get(transaction.getDate());

            AtomicBoolean dateAlreadyExists = new AtomicBoolean(false);
            sameDatedTransactions.forEach(transaction -> {
                if (transaction.getType().equals(this.transaction.getType()))
                    dateAlreadyExists.set(true);
            });

            if(dateAlreadyExists.get()) {
                sameDatedTransactions.forEach(storedTransactionType -> {
                    if (storedTransactionType.getType().equalsIgnoreCase(this.transaction.getType())) {
                        storedTransactionType.setAmount(String.valueOf(Double.parseDouble(this.transaction.getAmount()) +
                                Double.parseDouble(storedTransactionType.getAmount())));
                        logger.info("The transaction with the unique date '" + storedTransactionType.getDate() + "' and type '"
                                + storedTransactionType.getType() + "' were overwritten");
                    }
                });
            } else {
                sameDatedTransactions.add(transaction);
            }
        } else {
            List<Transaction> transactionList = new ArrayList<>();
            transactionList.add(transaction);
            persistedTransactions.put(transaction.getDate(), transactionList);
        }
        return this;
    }

    @Override
    public Map<String, List<Transaction>> getPersistedTransactions() {
        return persistedTransactions;
    }

    public Transaction getRequestedTransaction(){
        return transaction;
    }
}
