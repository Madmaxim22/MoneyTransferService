package com.example.moneytransferservice.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class Amount {
    @Min(value = 10, message = "Сумма перевода должна быть больше 10")
    private int value;

    @NotBlank(message = "Код валюты не может быть пустой")
    private String currency;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "Amount{" +
                "value=" + value +
                ", currency='" + currency + '\'' +
                '}';
    }
}
