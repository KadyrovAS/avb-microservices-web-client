package com.avb.controller;

import com.avb.model.AVBError;
import com.avb.model.AVBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@RestControllerAdvice
public class CompanyControllerAdvice{
    private static final Logger logger = LoggerFactory.getLogger(CompanyControllerAdvice.class);

    @ExceptionHandler(AVBException.class)
    public ResponseEntity<AVBError> handleCompanyException(AVBException e) {
        AVBError error = new AVBError(e.getCode(), e.getMessage());
        HttpStatus status = HttpStatus.valueOf(Integer.parseInt(error.getCode()));
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(WebClientResponseException.NotFound.class)
    public ResponseEntity<AVBError> handleWebClientNotFoundException(WebClientResponseException.NotFound e) {
        AVBError error = new AVBError(e.getResponseBodyAsString());
        HttpStatus status = HttpStatus.valueOf(Integer.parseInt(error.getCode()));
        return ResponseEntity.status(status).body(error);
    }

}