package com.example.dictionary_mmtr.enums;

import lombok.Getter;

@Getter
public enum DictionaryType {
    LATIN("dictionary_latin"),
    NUMBER("dictionary_number"),
    BACKSPACE("dictionary_backspace");

    private final String description;

    DictionaryType(String description) {
        this.description = description;
    }
}
