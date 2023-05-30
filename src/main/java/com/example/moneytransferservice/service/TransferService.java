package com.example.moneytransferservice.service;

import com.example.moneytransferservice.model.TransferCard;
import com.example.moneytransferservice.model.TransferCardDto;
import com.example.moneytransferservice.model.TransferCardDtoMapper;
import com.example.moneytransferservice.repository.Repository;
import com.example.moneytransferservice.response.Response;
import com.example.moneytransferservice.response.SuccessesTransfer;
import com.example.moneytransferservice.util.SendMessage;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TransferService {

    private final Repository repository;
    private final TransferCardDtoMapper mapper = Mappers.getMapper(TransferCardDtoMapper.class);

    public TransferService(Repository repository) {
        this.repository = repository;
    }

    public ResponseEntity<Response> transfer(TransferCardDto transferCardDto) {
        // Валидация карт
        cardValidation(transferCardDto);
        // Получение номера отправителя
        String userPhoneNumber = userPhoneNumber(transferCardDto.getCardToNumber());
        // Отправка сообщения с кодом на телефон
        String confirmationCode = SendMessage.sendMessage(userPhoneNumber);
        // Получаем id операции
        int operationId = repository.getId();
        // Создаем объект операции
        TransferCard card = mapper.transferCardDtoToTransferCard(transferCardDto);
        card.setConfirmationCode(confirmationCode);
        card.setOperationId(operationId);
        // Сохраняем объект
        repository.saveTransferCard(card);
        return new ResponseEntity<>(new SuccessesTransfer(operationId), HttpStatus.OK);
    }

    public boolean cardValidation(TransferCardDto transferCardDto) {
        if (!repository.checkCard(transferCardDto.getCardToNumber())) {
            throw new RuntimeException("Карта " + transferCardDto.getCardToNumber() + " не существует");
        }
        if (!repository.checkCard(transferCardDto)) {
            throw new RuntimeException("Карта " + transferCardDto.getCardFromNumber() + " не существует");
        }
        return true;
    }

    public String userPhoneNumber(String cardNumber) {
        return repository.userPhoneNumber(cardNumber);
    }
}
