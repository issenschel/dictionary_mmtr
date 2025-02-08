package com.example.dictionary_mmtr.exception;

public class KeyFoundException extends RuntimeException {
    public KeyFoundException() {
        super("error.duplicate_key");
    }
}
