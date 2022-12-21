package com.interview.fundrecs.repo;

import com.google.gson.Gson;
import com.interview.fundrecs.model.Transaction;
import com.interview.fundrecs.model.TransactionsAggregate;
import com.interview.fundrecs.model.TransactionsRepoPort;
import org.json.JSONArray;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class TransactionsWriteToJsonFileAdaptorRepo implements TransactionsRepoPort {
    private TransactionsAggregate transactionsAggregate = new TransactionsAggregate();
    private String destinationPath;

    public TransactionsWriteToJsonFileAdaptorRepo (String destinationPath){
        this.destinationPath = destinationPath;
    }

    @Override
    public void saveTransaction(List<Transaction> transaction) {
        Map<String, List<Transaction>> allTransactions = transactionsAggregate.addTransactions(transaction);
        Collection<List<Transaction>> transactionsLists = allTransactions.values();
        List<Transaction> transactions = new ArrayList<>();
        transactionsLists.forEach(list -> {
            list.forEach(tran -> {
                transactions.add(tran);
            });
        });

        JSONArray jsonArray = new JSONArray(transactions);
        try (PrintWriter out = new PrintWriter(new FileWriter(destinationPath))) {
            out.write(jsonArray.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Transaction findTransaction(String date, String type)  {
        return transactionsAggregate.getTransaction(date, type);
    }

    @Override
    public TransactionsAggregate getTransactionsAggregate(){
        return this.transactionsAggregate;
    }

    @Override
    public TransactionsAggregate loadAlreadyPersistedTransactions() throws IOException {
        if (Files.exists(new File(destinationPath).toPath())) {
            Reader reader = Files.newBufferedReader(Paths.get(destinationPath));
            Gson gson = new Gson();
            Transaction [] transactions = gson.fromJson(reader, Transaction[].class);
            if(transactions != null) {
                System.out.println("The retrieved transactions list size is: " + transactions.length);
                transactionsAggregate.addTransactions(Arrays.stream(transactions).collect(Collectors.toList()));
            }
            return transactionsAggregate;
        }
        return null;
    }
}
