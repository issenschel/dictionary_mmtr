package com.example.dictionary_mmtr.service;

import com.example.dictionary_mmtr.entity.DictionaryEntry;
import com.example.dictionary_mmtr.entity.DictionaryValue;
import com.example.dictionary_mmtr.repository.DictionaryValueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DictionaryValueService {

    private final DictionaryValueRepository dictionaryValueRepository;

    public Optional<DictionaryValue> findByValueAndDictionaryEntry(String key, DictionaryEntry dictionaryEntry){
        return dictionaryValueRepository.findByValueAndDictionaryEntry(key, dictionaryEntry);
    }

    public void createDictionaryValue(DictionaryEntry dictionaryEntry, String value) {
        DictionaryValue dictionaryValue = new DictionaryValue();
        dictionaryValue.setDictionaryEntry(dictionaryEntry);
        dictionaryValue.setValue(value);
        dictionaryValueRepository.save(dictionaryValue);
    }
}
