package com.interview.fundrecs.model;

public class Transaction {
    private String date;
    private String type;
    private String amount;

    public String getDate(){
        return date;
    }

    public String getType(){
        return type;
    }

    public String getAmount(){
        return amount;
    }

    public void setDate(String date){
        this.date = date;
    }

    public void setType(String type){
        this.type = type;
    }

    public void setAmount(String amount){
        this.amount = amount;
    }
}
