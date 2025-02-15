package com.example.dictionary_mmtr.service;

import com.example.dictionary_mmtr.dto.KeyValuePairDto;
import com.example.dictionary_mmtr.dto.KeyValuePairRequestDto;
import com.example.dictionary_mmtr.entity.BaseDictionary;
import com.example.dictionary_mmtr.exception.DictionaryException;
import com.example.dictionary_mmtr.exception.KeyFoundException;
import com.example.dictionary_mmtr.repository.BaseDictionaryRepository;
import com.example.dictionary_mmtr.validation.Validation;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class UniqueDictionaryService<T extends BaseDictionary> extends BaseFileDictionaryService<T> {

    public UniqueDictionaryService(BaseDictionaryRepository<T> baseDictionaryRepository, Validation validation,
                                   MessageSource messageSource, Class<T> dictionaryClass) {
        super(baseDictionaryRepository, validation, messageSource,dictionaryClass);
    }

    @Override
    public T addEntry(KeyValuePairRequestDto keyValuePairDto) {
        try {
            validate(keyValuePairDto.getKey());
            baseDictionaryRepository.findByKey(keyValuePairDto.getKey()).ifPresent(t -> {throw new KeyFoundException();});
            T baseDictionary = dictionaryClass.getDeclaredConstructor().newInstance();
            baseDictionary.setKey(keyValuePairDto.getKey());
            baseDictionary.setValue(keyValuePairDto.getValue());
            return baseDictionaryRepository.save(baseDictionary);
        } catch (Exception e) {
            throw new DictionaryException(e.getMessage());
        }
    }
}
