package com.avb.model;

public class AVBException extends RuntimeException {
    private final String code;

    public AVBException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
