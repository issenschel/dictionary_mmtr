package com.example.dictionary_mmtr.service;

import com.example.dictionary_mmtr.dto.KeyValuesDto;
import com.example.dictionary_mmtr.entity.DictionaryEntry;
import com.example.dictionary_mmtr.entity.DictionaryValue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DictionaryMapper {
    public KeyValuesDto convertToDictionaryDto(DictionaryEntry entry, boolean useLegacyFormat) {
        List<String> values = entry.getValues().stream()
                .map(DictionaryValue::getValue)
                .collect(Collectors.toList());

        if (useLegacyFormat) {
            return new KeyValuesDto(entry.getKey(), values.isEmpty() ? null : values.get(0));
        } else {
            return new KeyValuesDto(entry.getKey(), values);
        }
    }
}
