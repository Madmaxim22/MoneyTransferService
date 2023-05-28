package com.example.moneytransferservice.integration;

import com.example.moneytransferservice.model.Amount;
import com.example.moneytransferservice.model.TransferCardDto;
import com.example.moneytransferservice.response.SuccessesTransfer;
import com.example.moneytransferservice.response.error.ErrorInputData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationApplicationTest {

    TransferCardDto transferCardDto;
    Amount amount;

    @Autowired
    TestRestTemplate restTemplate;

    @Container
    private static final GenericContainer<?> backend = new GenericContainer<>("moneytransferservice-beckend:latest")
            .withExposedPorts(8080);

    @BeforeEach
    public void init() throws ParseException {
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
    void returnSuccessesTransferTest() {

        HttpEntity<TransferCardDto> entity = new HttpEntity<>(transferCardDto);

        ResponseEntity<SuccessesTransfer> response = restTemplate.postForEntity(
                "http://localhost:" + backend.getMappedPort(8080) + "/transfer",
                entity, SuccessesTransfer.class);

        assertNotNull(response.getBody());
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getOperationId(), is(1));
    }

    @Test
    void returnErrorInputDataTest() {
        transferCardDto.setCardToNumber("1111");
        HttpEntity<TransferCardDto> entity = new HttpEntity<>(transferCardDto);

        ResponseEntity<ErrorInputData> response = restTemplate.postForEntity(
                "http://localhost:" + backend.getMappedPort(8080) + "/transfer",
                entity, ErrorInputData.class);

        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getMessage());
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertThat(response.getBody().getId(), is(0));
        assertThat(response.getBody().getMessage(), is("Некорректный номер карты получателя"));
    }

    @Test
    void returnErrorTransferTest() {
        transferCardDto.setCardToNumber("1111111111111112");
        HttpEntity<TransferCardDto> entity = new HttpEntity<>(transferCardDto);

        ResponseEntity<ErrorInputData> response = restTemplate.postForEntity(
                "http://localhost:" + backend.getMappedPort(8080) + "/transfer",
                entity, ErrorInputData.class);

        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getMessage());
        assertThat(response.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));
        assertThat(response.getBody().getId(), is(0));
        assertThat(response.getBody().getMessage(), is("Карта " + transferCardDto.getCardToNumber() + " не существует"));
    }
}
