package com.example.moneytransferservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.Date;

public class TransferCardDto {

    @NotBlank(message = "Номер карты не может быть пустой")
    @Pattern(regexp = "[0-9]{16}", message = "Некорректный номер карты отправителя")
    private String cardFromNumber;

    @Future(message = "Поле CVV должно содержать дату, которая еще не наступила")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/yy")
    private Date cardFromValidTill;
    @NotBlank(message = "CVV карты не может быть пустой")
    @Pattern(regexp = "[0-9]{3}", message = "Некорректный CVV отправителя")
    private String cardFromCVV;
    @NotBlank(message = "Номер карты не может быть пустой")
    @Pattern(regexp = "[0-9]{16}", message = "Некорректный номер карты получателя")
    private String cardToNumber;
    @Valid
    private Amount amount;

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

    @Override
    public String toString() {
        return "TransferCardDto{" +
                "cardFromNumber='" + cardFromNumber + '\'' +
                ", cardFromValidTill=" + cardFromValidTill +
                ", cardFromCVV='" + cardFromCVV + '\'' +
                ", cardToNumber='" + cardToNumber + '\'' +
                ", amount=" + amount +
                '}';
    }
}
