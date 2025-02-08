package com.example.dictionary_mmtr.validation;

import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class LatinValidation implements Validation {

    public boolean validate(String input) {
        return input.matches("[a-zA-Z]+") && input.length() == 4;
    }

    public String getRequirements(){
        return "latin.validation.requirements";
    }

    public Function<String, String> getKeyTransformer(){
        return key -> key;
    }
}
