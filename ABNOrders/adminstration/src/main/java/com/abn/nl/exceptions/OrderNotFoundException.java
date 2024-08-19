package com.abn.nl.exceptions;

public class OrderNotFoundException  extends RuntimeException{
    public OrderNotFoundException(String message){
        super(message);
    }
}
