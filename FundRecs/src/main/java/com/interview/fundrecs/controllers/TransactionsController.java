package com.interview.fundrecs.controllers;

import com.interview.fundrecs.StartWebServiceApp;
import com.interview.fundrecs.model.Transaction;
import com.interview.fundrecs.model.TransactionsRepoPort;
import com.interview.fundrecs.repo.TransactionsWriteToJsonFileAdaptorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/transaction")
@Lazy
public class TransactionsController {

    private TransactionsRepoPort transactionsRepoPort;

    public TransactionsController() throws IOException {
        transactionsRepoPort =
                new TransactionsWriteToJsonFileAdaptorRepo(StartWebServiceApp.TRANSACTIONS_STORAGE_PATH);
        transactionsRepoPort.loadAlreadyPersistedTransactions();
        System.out.println(transactionsRepoPort.getTransactionsAggregate().getTransactionsCount() + " transactions retrieved ...");
    }

    @GetMapping("/get")
    public ResponseEntity<Transaction> getTransaction(@Valid @RequestBody Transaction transaction) {
        return ResponseEntity.ok(transactionsRepoPort.findTransaction(transaction.getDate(), transaction.getType()));
    }

    @PostMapping(value = "/submit", consumes = "application/json", produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<String> submitTransaction(@Valid @RequestBody List<Transaction> transaction){
        transactionsRepoPort.saveTransaction(transaction);
        return ResponseEntity.ok("Transaction was received");
    }
}
