package com.interview.fundrecs.model.validations;

import com.interview.fundrecs.model.Transaction;
import com.sun.media.sound.InvalidFormatException;

import java.util.List;
import java.util.Map;

public interface TransactionApprovalSteps {
    TransactionApprovalSteps validateDate() throws InvalidFormatException;
    TransactionApprovalSteps validateType();

    /**
     * A transaction is uniquely identified by date and type. If such a transaction already exists
     * in a file, then sum the amounts of 2 transactions but save only one of those.
     *
     * @return TransactionApprovalStep
     */
    TransactionApprovalSteps storeUniqueTransactionOrSumAmountAndOverwrite();
    Map<String, List<Transaction>> getPersistedTransactions();

    Transaction getRequestedTransaction();

}
