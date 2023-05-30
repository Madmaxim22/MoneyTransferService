package com.example.moneytransferservice.service;

import com.example.moneytransferservice.model.Amount;
import com.example.moneytransferservice.model.Confirm;
import com.example.moneytransferservice.model.TransferCardDto;
import com.example.moneytransferservice.response.SuccessesTransfer;
import com.example.moneytransferservice.response.error.ErrorInputData;
import com.example.moneytransferservice.response.error.ErrorTransfer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class OnlyWebLevelMockMvcUnitTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private TransferService transferService;
    @MockBean
    private ConfirmService confirmService;
    private TransferCardDto transferCardDto;
    private Amount amount;
    private Confirm confirm;

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
        confirm = new Confirm(1, "0000");
    }

    @Test
    public void givenCardTransferDto_whenValid_then200andOperationId() throws Exception {
        Mockito.when(transferService.transfer(Mockito.any())).thenReturn(new ResponseEntity<>(new SuccessesTransfer(1), HttpStatus.OK));

        this.mockMvc.perform(
                        post("/transfer")
                                .content(objectMapper.writeValueAsString(transferCardDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.operationId").value(1));
    }

    @Test
    public void givenCardTransferDto_whenNotValid_then400andMessage() throws Exception {
        Mockito.when(transferService.transfer(Mockito.any())).thenReturn(new ResponseEntity<>(new ErrorInputData("Некорректный номер карты получателя"), HttpStatus.BAD_REQUEST));

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
        Mockito.when(transferService.transfer(Mockito.any())).thenReturn(new ResponseEntity<>(new ErrorTransfer("Карта 1111111111111111 не существует"), HttpStatus.INTERNAL_SERVER_ERROR));

        this.mockMvc.perform(post("/transfer")
                        .content(objectMapper.writeValueAsString(transferCardDto))
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value("Карта " + transferCardDto.getCardFromNumber() + " не существует"))
                .andExpect(jsonPath("$.id").value(0));

    }

    @Test
    public void givenConfirm_whenValid_then200andOperationId() throws Exception {
        Mockito.when(confirmService.confirm(Mockito.any())).thenReturn(new ResponseEntity<>(new SuccessesTransfer(1), HttpStatus.OK));

        this.mockMvc.perform(post("/confirmOperation")
                        .content(objectMapper.writeValueAsString(confirm))
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk()).andExpect(content()
                        .contentType("application/json"))
                .andExpect(jsonPath("$.operationId").value(1));

    }

    @Test
    public void givenConfirm_whenNotValid_then400andMessage() throws Exception {
        Mockito.when(confirmService.confirm(Mockito.any())).thenReturn(new ResponseEntity<>(new ErrorTransfer("Номер операции должен быть положительным числом"), HttpStatus.BAD_REQUEST));

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
        Mockito.when(confirmService.confirm(Mockito.any())).thenReturn(new ResponseEntity<>(new ErrorTransfer("Неверный код подтвержения"), HttpStatus.INTERNAL_SERVER_ERROR));

        this.mockMvc.perform(post("/confirmOperation")
                        .content(objectMapper.writeValueAsString(confirm))
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value("Неверный код подтвержения"))
                .andExpect(jsonPath("$.id").value(0));

    }
}
