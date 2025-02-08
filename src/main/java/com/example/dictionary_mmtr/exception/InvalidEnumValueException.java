package com.example.dictionary_mmtr.exception;

import java.util.Arrays;
import java.util.stream.Collectors;

public class InvalidEnumValueException extends RuntimeException {
    private final String invalidValue;
    private final Class<? extends Enum<?>> enumType;

    public InvalidEnumValueException(String invalidValue, Class<? extends Enum<?>> enumType) {
        super("Invalid value for enum: " + invalidValue + ". Valid values: " + getValidValues(enumType));
        this.invalidValue = invalidValue;
        this.enumType = enumType;
    }

    public String getInvalidValue() {
        return invalidValue;
    }

    public Class<? extends Enum<?>> getEnumType() {
        return enumType;
    }

    private static String getValidValues(Class<? extends Enum<?>> enumType) {
        Enum<?>[] enumConstants = enumType.getEnumConstants();
        if (enumConstants == null) {
            return "No valid values";
        }
        return Arrays.stream(enumConstants)
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }
}
