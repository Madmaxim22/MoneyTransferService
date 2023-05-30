package com.example.moneytransferservice.integration;

import com.example.moneytransferservice.model.Amount;
import com.example.moneytransferservice.model.TransferCardDto;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class ControllerTransferMockMvcIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    TransferCardDto transferCardDto;
    Amount amount;

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
    public void givenCardTransferDto_whenValid_then200andOperationId() throws Exception {
        this.mockMvc.perform(post("/transfer")
                        .content(objectMapper.writeValueAsString(transferCardDto))
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk()).andExpect(content()
                        .contentType("application/json"))
                .andExpect(jsonPath("$.operationId").value(1));

    }

    @Test
    public void givenCardTransferDto_whenNotValid_then400andMessage() throws Exception {
        transferCardDto.setCardToNumber("111111");
        this.mockMvc.perform(post("/transfer")
                        .content(objectMapper.writeValueAsString(transferCardDto))
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value("Некорректный номер карты получателя"))
                .andExpect(jsonPath("$.id").value(0));

    }

    @Test
    public void givenCardTransferDto_whenCardNotFound_then500andMessage() throws Exception {
        transferCardDto.setCardToNumber("1111111122222222");
        this.mockMvc.perform(post("/transfer")
                        .content(objectMapper.writeValueAsString(transferCardDto))
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value("Карта " + transferCardDto.getCardToNumber() + " не существует"))
                .andExpect(jsonPath("$.id").value(0));

    }
}
