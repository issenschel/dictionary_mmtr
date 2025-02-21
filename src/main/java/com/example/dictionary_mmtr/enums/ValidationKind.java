package com.example.dictionary_mmtr.enums;

import com.example.dictionary_mmtr.validation.Validation;
import lombok.Getter;

@Getter
public enum ValidationKind {
    BACKSPACE,
    LATIN,
    NUMBER;

    private Validation validation;

    public void setValidation(Validation validation) {
        this.validation = validation;
    }

}