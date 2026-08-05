package com.bash.dash.exceptions;

public class Forbidden extends Exception{
    String status;
    public Forbidden(String message, String status){
        super(message);
        this.status = status;
    }
}