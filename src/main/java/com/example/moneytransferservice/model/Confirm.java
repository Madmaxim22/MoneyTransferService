package com.example.moneytransferservice.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class Confirm {

    @Positive(message = "Номер операции должен быть положительным числом")
    private int operationId;

    @NotBlank
    private String code;

    public Confirm(int operationId, String code) {
        this.operationId = operationId;
        this.code = code;
    }

    public int getOperationId() {
        return operationId;
    }

    public void setOperationId(int operationId) {
        this.operationId = operationId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
