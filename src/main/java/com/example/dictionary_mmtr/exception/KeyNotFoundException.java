package com.example.dictionary_mmtr.exception;

public class KeyNotFoundException extends RuntimeException{
    public KeyNotFoundException(){
        super("error.key_not_found");
    }
}
