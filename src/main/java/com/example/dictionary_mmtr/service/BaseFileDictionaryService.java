package com.example.dictionary_mmtr.service;

import com.example.dictionary_mmtr.dto.KeyValuePairDto;
import com.example.dictionary_mmtr.dto.KeyValuePairGroupDto;
import com.example.dictionary_mmtr.dto.KeyValuePairListWrapper;
import com.example.dictionary_mmtr.dto.ResponseDto;
import com.example.dictionary_mmtr.exception.DictionaryException;
import com.example.dictionary_mmtr.exception.KeyNotFoundException;
import com.example.dictionary_mmtr.exception.ValidationException;
import com.example.dictionary_mmtr.repository.DictionaryRepository;
import com.example.dictionary_mmtr.validation.Validation;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public abstract class BaseFileDictionaryService implements DictionaryService {
    protected final DictionaryRepository dictionaryRepository;
    protected final Validation validation;
    private final MessageSource messageSource;

    public BaseFileDictionaryService(DictionaryRepository DictionaryRepository, Validation validation, MessageSource messageSource) {
        this.dictionaryRepository = DictionaryRepository;
        this.validation = validation;
        this.messageSource = messageSource;
    }

    public List<KeyValuePairDto> findAll(){
        try {
            return dictionaryRepository.findAll();
        } catch (IOException e) {
            throw new DictionaryException(e.getMessage());
        }
    }

    public ResponseDto removeEntryByKey(String key){
        try {
            validate(key);
            if (!dictionaryRepository.removeEntryByKey(key)) {
                throw new KeyNotFoundException();

            }
            return new ResponseDto(messageSource.getMessage("success.entry.removed", null, LocaleContextHolder.getLocale()));

        } catch (IOException e) {
            throw new DictionaryException(e.getMessage());
        }
    }

    public KeyValuePairDto searchEntryByKey(String key){
        try {
            validate(key);
            return dictionaryRepository.searchEntryByKey(key).orElseThrow(KeyNotFoundException::new);
        } catch (IOException e) {
            throw new DictionaryException(e.getMessage());
        }
    }

    public KeyValuePairDto addEntry(String key, String value){
        try {
            validate(key);
            KeyValuePairDto keyValuePairDto = new KeyValuePairDto(key, value);
            return dictionaryRepository.addEntry(keyValuePairDto);
        } catch (IOException e) {
            throw new DictionaryException(e.getMessage());
        }
    }

    public KeyValuePairGroupDto getPage(int page, int size){
        try {
            return dictionaryRepository.getPage(page, size);
        } catch (IOException e) {
            throw new DictionaryException(e.getMessage());
        }
    }

    public void getDictionaryAsXML(OutputStream outputStream) throws DictionaryException, IOException {
        List<KeyValuePairDto> entries = findAll();
        KeyValuePairListWrapper wrapper = new KeyValuePairListWrapper(entries);
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.writeValue(outputStream, wrapper);
    }


    protected void validate(String key) {
        if (!validation.validate(key)) {
            throw new ValidationException(validation.getRequirements());
        }
    }
}
