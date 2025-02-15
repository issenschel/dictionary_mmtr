package com.example.dictionary_mmtr.service;

import com.example.dictionary_mmtr.entity.BaseDictionary;
import com.example.dictionary_mmtr.repository.BaseDictionaryRepository;
import com.example.dictionary_mmtr.validation.Validation;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class DuplicateDictionaryService<T extends BaseDictionary> extends BaseFileDictionaryService<T> {
    public DuplicateDictionaryService(BaseDictionaryRepository<T> baseDictionaryRepository, Validation validation,
                                      MessageSource messageSource, Class<T> dictionaryClass) {
        super(baseDictionaryRepository, validation, messageSource,dictionaryClass);
    }
}
