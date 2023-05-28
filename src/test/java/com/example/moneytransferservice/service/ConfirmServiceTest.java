package com.example.moneytransferservice.service;

import com.example.moneytransferservice.model.Amount;
import com.example.moneytransferservice.model.TransferCard;
import com.example.moneytransferservice.repository.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


class ConfirmServiceTest {

    Repository repository;
    ConfirmService service;
    @BeforeEach
    public void init() {
        repository = new Repository();
        service = new ConfirmService(repository);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1000, 10",
            "5000, 50",
            "6900, 69"})
    void commissionTest(long value, long result) {
        assertThat(service.commission(value), equalTo(result));
    }

    @Test
    void increaseAndDecreaseBalanceTest() throws ParseException {
        TransferCard card = new TransferCard();
        card.setCardFromNumber("1111111111111111");
        card.setCardToNumber("2222222222222222");
        card.setCardFromCVV("123");
        card.setCardFromValidTill(String.valueOf(new SimpleDateFormat("MM/yy").parse("11/23")));
        Amount amount = new Amount();
        amount.setValue(1000);
        amount.setCurrency("RUR");
        card.setAmount(amount);
        card.setConfirmationCode("0000");
        card.setOperationId(1);

        service.increaseAndDecreaseBalance(10, card);

        assertThat(repository.getCard("1111111111111111").getBalance(), equalTo(198_990L));
        assertThat(repository.getCard("2222222222222222").getBalance(), equalTo(201_000L));
    }
}