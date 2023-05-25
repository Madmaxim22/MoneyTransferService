package com.example.moneytransferservice.advice;

import com.example.moneytransferservice.response.Response;
import com.example.moneytransferservice.response.error.ErrorInputData;
import com.example.moneytransferservice.response.error.ErrorTransfer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ExceptionHandlerAdvice.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> manveHandler(MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult().getAllErrors().stream().map(fe -> String
                .format("%s", fe.getDefaultMessage()))
                .toList();
        String response = String.join(", ", errors);
        logger.error(response);
        return new ResponseEntity<>(new ErrorInputData(response), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> eHandler(Exception e) {
        logger.error(e.getMessage());
        return new ResponseEntity<>(new ErrorTransfer(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
