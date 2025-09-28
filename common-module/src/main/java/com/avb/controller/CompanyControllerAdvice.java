package com.avb.controller;

import com.avb.model.AVBError;
import com.avb.model.AVBException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice
public class CompanyControllerAdvice {

    @ExceptionHandler(AVBException.class)
    public ResponseEntity<AVBError> handleCompanyException(AVBException e) {
        AVBError error = new AVBError(e.getCode(), e.getMessage());
        HttpStatus status = HttpStatus.valueOf(Integer.parseInt(error.getCode()));
        return ResponseEntity.status(status).body(error);
         }
    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public ResponseEntity<AVBError>handlerHttpClientErrorException(HttpClientErrorException.NotFound e){
        AVBError error = new AVBError(e.getResponseBodyAsString());
        HttpStatus status = HttpStatus.valueOf(Integer.parseInt(error.getCode()));
        return ResponseEntity.status(status).body(error);
    }
}