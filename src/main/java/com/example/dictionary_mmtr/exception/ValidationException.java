package com.example.dictionary_mmtr.exception;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException{

    public ValidationException(String error){
        super(error);
    }
}
