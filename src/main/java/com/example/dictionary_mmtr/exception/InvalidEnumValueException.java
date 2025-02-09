package com.example.dictionary_mmtr.exception;

import lombok.Getter;

import java.util.Arrays;
import java.util.stream.Collectors;

@Getter
public class InvalidEnumValueException extends RuntimeException {
    private final String invalidValue;
    private final Class<? extends Enum<?>> enumType;

    public InvalidEnumValueException(String invalidValue, Class<? extends Enum<?>> enumType) {
        this.invalidValue = invalidValue;
        this.enumType = enumType;
    }

    public static String getValidValues(Class<? extends Enum<?>> enumType) {
        Enum<?>[] enumConstants = enumType.getEnumConstants();
        return Arrays.stream(enumConstants)
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }
}
