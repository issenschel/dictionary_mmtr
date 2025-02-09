package com.example.dictionary_mmtr.exception;

import lombok.Getter;

@Getter
public class RemoveEntryException extends RuntimeException{
    private final String error;

    public RemoveEntryException(String error){
        super("error.remove_entry");
        this.error = error;
    }

}
