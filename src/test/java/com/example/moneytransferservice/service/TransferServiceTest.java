package com.example.moneytransferservice.service;

import com.example.moneytransferservice.model.Amount;
import com.example.moneytransferservice.model.TransferCard;
import com.example.moneytransferservice.model.TransferCardDto;
import com.example.moneytransferservice.repository.Repository;
import com.example.moneytransferservice.response.Response;
import com.example.moneytransferservice.response.SuccessesTransfer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransferServiceTest {

    TransferService service;
    Repository repository;
    TransferCardDto transferCardDto;
    Amount amount;
    @BeforeEach
    public void init() throws ParseException {
        repository = new Repository();
        service = new TransferService(repository);
        transferCardDto = new TransferCardDto();
        transferCardDto.setCardFromNumber("1111111111111111");
        transferCardDto.setCardToNumber("2222222222222222");
        transferCardDto.setCardFromCVV("123");
        transferCardDto.setCardFromValidTill(new SimpleDateFormat("MM/yy").parse("11/23"));
        transferCardDto.setAmount(amount);
        amount = new Amount();
        amount.setValue(1000);
        amount.setCurrency("RUR");
    }

    @Test
    void transferTest() {
        ResponseEntity<Response> responseEntity = service.transfer(transferCardDto);

        Response response = new SuccessesTransfer("1");

        assertThat(responseEntity.getBody().toString(), hasToString(response.toString()));
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    void transferExceptionTest() {
        ResponseEntity<Response> responseEntity = service.transfer(transferCardDto);

        Response response = new SuccessesTransfer("1");

        assertThat(responseEntity.getBody().toString(), hasToString(response.toString()));
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    void cardValidationTest() {
        assertTrue(service.cardValidation(transferCardDto));
    }

    @Test
    void cardValidationExceptionTest() {
        transferCardDto.setCardFromCVV("111");
        Exception exception = assertThrows(RuntimeException.class, () -> service.cardValidation(transferCardDto));

        String expectedMessage = "Карта 1111111111111111 не существует";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void userPhoneNumberTest() {
        String cardNumber = "1111111111111111";
        String userPhoneNumber = service.userPhoneNumber(cardNumber);

        assertThat(userPhoneNumber, equalTo("89267091322"));
    }
}