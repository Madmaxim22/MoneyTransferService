package com.example.moneytransferservice.repository;

import com.example.moneytransferservice.exception.NotFoundException;
import com.example.moneytransferservice.model.Card;
import com.example.moneytransferservice.model.TransferCard;
import com.example.moneytransferservice.model.TransferCardDto;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@org.springframework.stereotype.Repository
public class Repository {


    private final CopyOnWriteArrayList<Card> cards = new CopyOnWriteArrayList<>(
            new Card[]{
                    new Card("1111111111111111", "11/23", "123", 200_000, "89267091322"),
                    new Card("2222222222222222", "11/23", "321", 200_000, "89193156893")
            });

    private final CopyOnWriteArrayList<TransferCard> transferCards = new CopyOnWriteArrayList<>();
    private final AtomicLong operationId = new AtomicLong();


    public boolean checkCard(String cardNumber) {
        for (Card card : cards) {
            if(card.getCardNumber().equals(cardNumber)) return true;
        }
        return false;
    }

    public boolean checkCard(TransferCardDto transferCardDto) {
        for (Card card : cards) {
            if (card.getCardNumber().equals(transferCardDto.getCardFromNumber()) &&
                    card.getCardFromCVV().equals(transferCardDto.getCardFromCVV())) {
                return true;
            }
        }
        return false;
    }

    public String userPhoneNumber(String cardNumber) {
        Card card = cards.stream()
                .filter(c -> c.getCardNumber().equals(cardNumber))
                .findAny()
                .orElseThrow(() -> new NotFoundException("У карты " + cardNumber + " отсутствует номер телефона"));
        return card.getUserPhoneNumber();
    }

    public long getId() {
        return operationId.incrementAndGet();
    }

    public boolean saveTransferCard(TransferCard transferCard) {
        return transferCards.add(transferCard);
    }

    public Card getCard(String cardNumber) {
        return cards.stream()
                .filter(c -> c.getCardNumber().equals(cardNumber))
                .findAny()
                .orElseThrow(NotFoundException::new);
    }

    public TransferCard getTransferCard(String operationId) {
        TransferCard transferCard = transferCards.stream()
                .filter(c -> c.getOperationId().equals(operationId))
                .findAny()
                .orElseThrow(NotFoundException::new);
        return transferCard;
    }
}
