package com.example.dictionary_mmtr.validation;

import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class BackspaceValidation implements Validation{

    public boolean validate(String input) {
        return input.matches("[a-zA-Z#]*");
    }

    public String getRequirements(){
        return "backspace.validation.requirements";
    }

    public Function<String, String> getKeyTransformer(){
        return key -> {
            StringBuilder processedKey = new StringBuilder();
            for (char ch : key.toCharArray()) {
                if (ch == '#') {
                    if (processedKey.length() > 0) {
                        processedKey.deleteCharAt(processedKey.length() - 1);
                    }
                } else {
                    processedKey.append(ch);
                }
            }
            return processedKey.toString();
        };
    }
}
