package com.example.moneytransferservice.controller;

import com.example.moneytransferservice.model.Confirm;
import com.example.moneytransferservice.model.TransferCardDto;
import com.example.moneytransferservice.response.Response;
import com.example.moneytransferservice.service.ConfirmService;
import com.example.moneytransferservice.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@CrossOrigin
public class Controller {

    private final TransferService transferService;
    private final ConfirmService confirmService;

    public Controller(TransferService transferService, ConfirmService confirmService) {
        this.transferService = transferService;
        this.confirmService = confirmService;
    }

    @PostMapping("transfer")
    public ResponseEntity<Response> transfer(@RequestBody @Valid TransferCardDto transferCardDto) {
        return transferService.transfer(transferCardDto);
    }

    @PostMapping("confirmOperation")
    public ResponseEntity<Response> confirm(@RequestBody @Valid Confirm confirm) {
        return confirmService.confirm(confirm);
    }
}