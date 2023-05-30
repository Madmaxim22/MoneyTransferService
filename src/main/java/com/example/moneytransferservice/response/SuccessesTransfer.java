package com.example.moneytransferservice.response;

public class SuccessesTransfer implements Response {
    private int operationId;

    public SuccessesTransfer(int operationId) {
        this.operationId = operationId;
    }

    public SuccessesTransfer() {
    }

    public int getOperationId() {
        return operationId;
    }

    public void setOperationId(int operationId) {
        this.operationId = operationId;
    }

    @Override
    public String toString() {
        return "SuccessesTransfer{" +
                "operationId='" + operationId + '\'' +
                '}';
    }
}
