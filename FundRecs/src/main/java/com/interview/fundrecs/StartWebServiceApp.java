package com.interview.fundrecs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class StartWebServiceApp {
    public static String TRANSACTIONS_STORAGE_PATH;

    public static void main(String [] args) {
        if(args[0] == null){
            System.out.println("Please run the app with the first input arg. being the path of the " +
                    "transactions.json storage file");
        } else {
            System.out.println("The input arg found is : " + args[0]);

            TRANSACTIONS_STORAGE_PATH = args[0];
            SpringApplication.run(StartWebServiceApp.class, args);
        }
    }
}