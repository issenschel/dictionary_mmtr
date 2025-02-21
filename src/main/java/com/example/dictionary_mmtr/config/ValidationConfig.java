package com.example.dictionary_mmtr.config;

import com.example.dictionary_mmtr.enums.ValidationKind;
import com.example.dictionary_mmtr.validation.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidationConfig {

    @Bean
    public ValidationKind backspaceValidationType(BackspaceValidation backspaceValidation) {
        ValidationKind.BACKSPACE.setValidation(backspaceValidation);
        return ValidationKind.BACKSPACE;
    }

    @Bean
    public ValidationKind latinValidationType(LatinValidation latinValidation) {
        ValidationKind.LATIN.setValidation(latinValidation);
        return ValidationKind.LATIN;
    }

    @Bean
    public ValidationKind numberValidationType(NumberValidation numberValidation) {
        ValidationKind.NUMBER.setValidation(numberValidation);
        return ValidationKind.NUMBER;
    }
}