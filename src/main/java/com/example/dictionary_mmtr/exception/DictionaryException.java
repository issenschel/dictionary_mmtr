package com.example.dictionary_mmtr.exception;

import lombok.Getter;

@Getter
public class DictionaryException extends RuntimeException {
    private final String error;

    public DictionaryException(String error){
        super("error.dictionary");
        this.error = error;
    }

}
