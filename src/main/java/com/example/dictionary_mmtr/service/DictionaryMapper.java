package com.example.dictionary_mmtr.service;

import com.example.dictionary_mmtr.dto.KeyValuePairDto;
import com.example.dictionary_mmtr.entity.DictionaryEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DictionaryMapper {
    public List<KeyValuePairDto> convertToDictionaryDto(DictionaryEntry entry) {
        return entry.getValues().stream()
                .map(dictionaryValue -> new KeyValuePairDto(entry.getKey(), dictionaryValue.getValue(), entry.getDictionaryType().getName()))
                .collect(Collectors.toList());
    }
}
