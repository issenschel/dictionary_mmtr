package com.example.dictionary_mmtr.exception;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException{
    private final String error;

    public ValidationException(String error){
        super("error.validation");
        this.error = error;
    }
}
