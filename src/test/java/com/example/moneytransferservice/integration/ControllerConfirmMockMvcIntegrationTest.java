package com.example.moneytransferservice.integration;

import com.example.moneytransferservice.model.Amount;
import com.example.moneytransferservice.model.Confirm;
import com.example.moneytransferservice.model.TransferCard;
import com.example.moneytransferservice.repository.Repository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ControllerConfirmMockMvcIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private Repository repository;
    private Confirm confirm;
    private TransferCard transferCard;
    private Amount amount;

    @BeforeEach
    public void init() throws ParseException {
        confirm = new Confirm(1, "0000");
        amount = new Amount();
        amount.setValue(1000);
        amount.setCurrency("RUR");
        transferCard = new TransferCard();
        transferCard.setCardFromNumber("1111111111111111");
        transferCard.setCardToNumber("2222222222222222");
        transferCard.setCardFromCVV("123");
        transferCard.setCardFromValidTill(new SimpleDateFormat("MM/yy").parse("11/23"));
        transferCard.setAmount(amount);
        transferCard.setConfirmationCode("0000");
        transferCard.setOperationId(1);
    }

    @AfterEach
    public void clear() {
        repository.getTransferCards().clear();
    }

    @Test
    public void givenConfirm_whenValid_then200andOperationId() throws Exception {
        repository.saveTransferCard(transferCard);

        this.mockMvc.perform(post("/confirmOperation")
                        .content(objectMapper.writeValueAsString(confirm))
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk()).andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.operationId").value(1));

    }

    @Test
    public void givenConfirm_whenNotValid_then400andMessage() throws Exception {
        confirm.setOperationId(-1);
        repository.saveTransferCard(transferCard);

        this.mockMvc.perform(post("/confirmOperation")
                        .content(objectMapper.writeValueAsString(confirm))
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value("Номер операции должен быть положительным числом"))
                .andExpect(jsonPath("$.id").value(0));

    }

    @Test
    public void givenConfirm_whenCardNotFound_then500andMessage() throws Exception {
        transferCard.setConfirmationCode("1111");
        repository.saveTransferCard(transferCard);

        this.mockMvc.perform(post("/confirmOperation")
                        .content(objectMapper.writeValueAsString(confirm))
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value("Неверный код подтвержения"))
                .andExpect(jsonPath("$.id").value(0));

    }
}
