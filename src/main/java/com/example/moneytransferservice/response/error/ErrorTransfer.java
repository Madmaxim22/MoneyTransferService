package com.example.moneytransferservice.response.error;

import com.example.moneytransferservice.response.Response;

public class ErrorTransfer implements Response {
    private String message;
    private int id = 0;

    public ErrorTransfer() {
    }

    public ErrorTransfer(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
