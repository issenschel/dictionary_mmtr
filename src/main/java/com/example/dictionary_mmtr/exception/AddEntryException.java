package com.example.dictionary_mmtr.exception;

import lombok.Getter;

@Getter
public class AddEntryException extends RuntimeException{
    private final String error;

    public AddEntryException(String error){
        super("error.add.entry");
        this.error = error;
    }

}
