package com.example.dictionary_mmtr.exception;

public class DictionaryFoundException extends RuntimeException {
    public DictionaryFoundException() {
        super("error.dictionary.found");
    }
}