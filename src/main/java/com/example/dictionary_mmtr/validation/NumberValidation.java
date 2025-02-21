package com.example.dictionary_mmtr.validation;

import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class NumberValidation implements Validation {

    public boolean validate(String input){
        return input.matches("[0-9]+") && input.length() == 5;
    }

    public String getRequirements(){
        return "number.validation.requirements";
    }

    public Function<String, String> getKeyTransformer(){
        return key -> key;
    }
}
