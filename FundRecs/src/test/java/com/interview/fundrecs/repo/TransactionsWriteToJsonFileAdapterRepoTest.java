package com.interview.fundrecs.repo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.*;
import com.interview.fundrecs.model.Transaction;
import com.interview.fundrecs.model.TransactionsAggregate;
import com.interview.fundrecs.model.TransactionsRepoPort;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class TransactionsWriteToJsonFileAdapterRepoTest {

    private TransactionsRepoPort transactionsRepoPort = new TransactionsWriteToJsonFileAdaptorRepo(
                        "target/TestTransactionStore/transactions.json");

    @BeforeClass
    public static void setup() {
        File theDir = new File("target/TestTransactionStore/");
        if (!theDir.exists()){
            theDir.mkdirs();
        }
    }

    @Before
    public void setUp() throws IOException {
        File file = new File("target/TestTransactionStore/transactions.json");
        Files.deleteIfExists(file.toPath());
    }


    @Test
    public void saveSingleTransactionTest() throws IOException {
        Transaction transaction1 = new Transaction();
        transaction1.setDate("02-02-2020");
        transaction1.setAmount("22.22");
        transaction1.setType("credit");
        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(transaction1);

        transactionsRepoPort.saveTransaction(transactionList);
        String data = new String(Files.readAllBytes(Paths.get("target/TestTransactionStore/transactions.json")));
        JsonElement jsonElement = JsonParser.parseString(data);
        if (jsonElement instanceof JsonObject) {
            JsonObject  jobject = jsonElement.getAsJsonObject();
            //System.out.println(jobject.toString());
        } else if (jsonElement instanceof JsonArray) {
            JsonArray  jarray = jsonElement.getAsJsonArray();
            System.out.println(jarray.toString());
            Assert.assertEquals("Check the json array is written to file as expected",
                    "[{\"date\":\"02-02-2020\",\"amount\":\"22.22\",\"type\":\"credit\"}]", jarray.toString());
        }
    }

    @Test
    public void saveTransactionToFileWithOneTransactionAlreadyAdded(){
        Transaction transaction1 = new Transaction();
        transaction1.setDate("02-02-2020");
        transaction1.setAmount("22.22");
        transaction1.setType("credit");
        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(transaction1);

        // Saving dummy initial transaction to file
        transactionsRepoPort.saveTransaction(transactionList);

        Transaction transaction2 = new Transaction();
        transaction2.setDate("02-02-2021");
        transaction2.setAmount("22.22");
        transaction2.setType("credit");
        transactionsRepoPort.saveTransaction(transactionList);
    }

    @Test
    public void checkLoadAlreadyPersistedTransactionsInAggregate_IsSuccessful() throws IOException {
        Transaction transaction1 = new Transaction();
        transaction1.setDate("02-02-2020");
        transaction1.setAmount("22.22");
        transaction1.setType("credit");
        Transaction transaction2 = new Transaction();
        transaction2.setDate("02-02-2021");
        transaction2.setAmount("22.22");
        transaction2.setType("credit");
        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(transaction1);
        transactionList.add(transaction2);

        try (PrintWriter out = new PrintWriter(new FileWriter("target/TestTransactionStore/transactions.json"))) {
            Gson gson = new Gson();
            String jsonString = gson.toJson(transactionList);
            System.out.println(jsonString);
            out.write(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Saving dummy initial transaction to file
        transactionsRepoPort.saveTransaction(transactionList);
        TransactionsAggregate transactionsAggregate = transactionsRepoPort.loadAlreadyPersistedTransactions();
        Assert.assertTrue("Check the aggregate transaction count after loading them from the file",
                2 == transactionsAggregate.getTransactionsCount());
    }
}
