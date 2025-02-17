package com.example.dictionary_mmtr.exception;

public class DictionaryNotFoundException extends RuntimeException {
    public DictionaryNotFoundException() {
        super("error.dictionary.not.found");
    }
}
