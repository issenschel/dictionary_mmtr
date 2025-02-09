package com.example.dictionary_mmtr.service;

import com.example.dictionary_mmtr.dto.KeyValuePairDto;
import com.example.dictionary_mmtr.exception.AddEntryException;
import com.example.dictionary_mmtr.exception.KeyFoundException;
import com.example.dictionary_mmtr.repository.DictionaryRepository;
import com.example.dictionary_mmtr.validation.Validation;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class UniqueDictionaryService extends BaseFileDictionaryService {

    public UniqueDictionaryService(DictionaryRepository dictionaryRepository, Validation validation, MessageSource messageSource) {
        super(dictionaryRepository, validation, messageSource);
    }

    @Override
    public KeyValuePairDto addEntry(String key, String value) {
        try {
            validate(key);
            dictionaryRepository.searchEntryByKey(key).ifPresent(t -> {
                throw new KeyFoundException();
            });

            KeyValuePairDto keyValuePairDto = new KeyValuePairDto(key, value);
            return dictionaryRepository.addEntry(keyValuePairDto);
        } catch (IOException e) {
            throw new AddEntryException(e.getMessage());
        }
    }
}
