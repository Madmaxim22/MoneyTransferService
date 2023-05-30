package com.example.moneytransferservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

public class Card {

    private String cardNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/yy")
    private Date cardFromValidTill;
    private String cardFromCVV;
    private AtomicLong balance = new AtomicLong();
    private String userPhoneNumber;

    public Card(String cardNumber, String cardFromValidTill, String cardFromCVV, long balance, String userPhoneNumber) {
        this.cardNumber = cardNumber;
        try {
            this.cardFromValidTill = new SimpleDateFormat("MM/yy").parse(cardFromValidTill);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        this.cardFromCVV = cardFromCVV;
        this.balance.set(balance);
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Date getCardFromValidTill() {
        return cardFromValidTill;
    }

    public void setCardFromValidTill(Date cardFromValidTill) {
        this.cardFromValidTill = cardFromValidTill;
    }

    public String getCardFromCVV() {
        return cardFromCVV;
    }

    public void setCardFromCVV(String cardFromCVV) {
        this.cardFromCVV = cardFromCVV;
    }

    public long getBalance() {
        return balance.get();
    }

    public long increaseBalance(long value) {
        return balance.addAndGet(value);
    }

    public long decreaseBalance(long value) {
        return balance.getAndAdd(-value);
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }
}
