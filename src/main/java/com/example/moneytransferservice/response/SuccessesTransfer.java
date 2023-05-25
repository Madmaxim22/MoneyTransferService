package com.example.moneytransferservice.response;

public class SuccessesTransfer implements Response{
    private String operationId;

    public SuccessesTransfer(String operationId) {
        this.operationId = operationId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    @Override
    public String toString() {
        return "SuccessesTransfer{" +
                "operationId='" + operationId + '\'' +
                '}';
    }
}
