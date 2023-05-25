package com.example.moneytransferservice.service;

import com.example.moneytransferservice.model.Card;
import com.example.moneytransferservice.model.Confirm;
import com.example.moneytransferservice.model.TransferCard;
import com.example.moneytransferservice.repository.Repository;
import com.example.moneytransferservice.response.Response;
import com.example.moneytransferservice.response.SuccessesTransfer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

@org.springframework.stereotype.Service
public class ConfirmService {
    private final Repository repository;
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ConfirmService.class);
    private final int COMMISSION = 1;
    public ConfirmService(Repository repository) {
        this.repository = repository;
    }

    public ResponseEntity<Response> confirm(Confirm confirm) {
        // получение id операции перевода
        TransferCard transferCard = repository.getTransferCard(confirm.getOperationId());
        // сравнение кодов
        if(!Objects.equals(confirm.getCode(), transferCard.getConfirmationCode())) {
             throw new RuntimeException("Неверный код подтвержения");
        }
        long commissionTransfer = commission(transferCard.getAmount().getValue());
        increaseAndDecreaseBalance(commissionTransfer, transferCard);
        logger.info("карта списания: {}, карта пополнения: {}, сумма: {}, коммисия: {}, successes",
                transferCard.getCardFromNumber(), transferCard.getCardToNumber(),
                transferCard.getAmount().getValue(), commissionTransfer);
        return new ResponseEntity<>(new SuccessesTransfer(confirm.getOperationId()), HttpStatus.OK);
    }

    long commission(long value) {
        return value / 100 * COMMISSION;
    }

    void increaseAndDecreaseBalance(long commission, TransferCard card) {
        // вычитание процента
        long amountCommission = card.getAmount().getValue() + commission;
        Card debitCard = repository.getCard(card.getCardFromNumber());
        Card creditCard = repository.getCard(card.getCardToNumber());
        // уменьшение баланса на одной карте
        debitCard.decreaseBalance(amountCommission);
        // увеличение баланса на другой карте
        creditCard.increaseBalance(card.getAmount().getValue());

    }
}
