package com.example.dictionary_mmtr.utility;

import com.example.dictionary_mmtr.exception.InvalidEnumValueException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;

@Component
public class StringToEnumConverterFactory implements ConverterFactory<String, Enum<?>> {

    public static class StringToEnumConverter<T extends Enum<T>> implements Converter<String, T> {
        private final Class<T> enumType;

        public StringToEnumConverter(Class<T> enumType) {
            this.enumType = enumType;
        }

        @Override
        public T convert(String source) {
            try {
                return Enum.valueOf(enumType, source.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new InvalidEnumValueException(source, enumType);
            }
        }
    }

    @Override
    public <T extends Enum<?>> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToEnumConverter(targetType);
    }
}
