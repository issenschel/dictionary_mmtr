package com.example.dictionary_mmtr.exception;

public class RemoveEntryException extends RuntimeException{
    public RemoveEntryException(){
        super("error.remove_entry");
    }

}
