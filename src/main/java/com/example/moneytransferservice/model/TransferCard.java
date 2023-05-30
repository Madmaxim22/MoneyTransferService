package com.example.moneytransferservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class TransferCard {

    private String cardFromNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/yy")
    private Date cardFromValidTill;
    private String cardFromCVV;
    private String cardToNumber;
    private Amount amount;
    private int operationId;
    private String confirmationCode;

    public String getCardFromNumber() {
        return cardFromNumber;
    }

    public void setCardFromNumber(String cardFromNumber) {
        this.cardFromNumber = cardFromNumber;
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

    public String getCardToNumber() {
        return cardToNumber;
    }

    public void setCardToNumber(String cardToNumber) {
        this.cardToNumber = cardToNumber;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public int getOperationId() {
        return operationId;
    }

    public void setOperationId(int operationId) {
        this.operationId = operationId;
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    @Override
    public String toString() {
        return "TransferCard{" +
                "cardFromNumber='" + cardFromNumber + '\'' +
                ", cardFromValidTill='" + cardFromValidTill + '\'' +
                ", cardFromCVV='" + cardFromCVV + '\'' +
                ", cardToNumber='" + cardToNumber + '\'' +
                ", amount=" + amount +
                ", operationId='" + operationId + '\'' +
                ", confirmationCode='" + confirmationCode + '\'' +
                '}';
    }
}
