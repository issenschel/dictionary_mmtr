package com.example.dictionary_mmtr.service;

import com.example.dictionary_mmtr.repository.DictionaryRepository;
import com.example.dictionary_mmtr.validation.Validation;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class DuplicateDictionaryService extends BaseFileDictionaryService {
    public DuplicateDictionaryService(DictionaryRepository dictionaryRepository, Validation validation, MessageSource messageSource) {
        super(dictionaryRepository, validation,messageSource);
    }
}
