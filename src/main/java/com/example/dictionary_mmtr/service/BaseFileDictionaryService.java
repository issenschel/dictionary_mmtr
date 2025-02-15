package com.example.dictionary_mmtr.service;

import com.example.dictionary_mmtr.dto.KeyValuePairGroupDto;
import com.example.dictionary_mmtr.dto.KeyValuePairRequestDto;
import com.example.dictionary_mmtr.dto.ResponseDto;
import com.example.dictionary_mmtr.entity.BaseDictionary;
import com.example.dictionary_mmtr.exception.DictionaryException;
import com.example.dictionary_mmtr.exception.KeyNotFoundException;
import com.example.dictionary_mmtr.exception.ValidationException;
import com.example.dictionary_mmtr.repository.BaseDictionaryRepository;
import com.example.dictionary_mmtr.validation.Validation;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

public abstract class BaseFileDictionaryService<T extends BaseDictionary> implements DictionaryService {

    protected final BaseDictionaryRepository<T> baseDictionaryRepository;
    protected final Validation validation;
    protected final MessageSource messageSource;
    protected final Class<T> dictionaryClass;

    public BaseFileDictionaryService(BaseDictionaryRepository<T> baseDictionaryRepository, Validation validation,
                                     MessageSource messageSource, Class<T> dictionaryClass) {
        this.baseDictionaryRepository = baseDictionaryRepository;
        this.validation = validation;
        this.messageSource = messageSource;
        this.dictionaryClass = dictionaryClass;
    }

    public KeyValuePairGroupDto getPage(int page, int size) {
        KeyValuePairGroupDto keyValuePairGroupDto = new KeyValuePairGroupDto();
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<T> baseDictionaries = baseDictionaryRepository.findAll(pageRequest);
        int totalPages = baseDictionaries.getTotalPages();
        keyValuePairGroupDto.setDictionary(baseDictionaries.getContent());
        keyValuePairGroupDto.setCount(totalPages);
        return keyValuePairGroupDto;
    }

    @Transactional
    public ResponseDto removeEntryByKey(String key) {
        validate(key);
        baseDictionaryRepository.deleteByKey(key);
        return new ResponseDto(messageSource.getMessage("success.entry.removed", null, LocaleContextHolder.getLocale()));
    }

    public T searchEntryByKey(String key) {
        validate(key);
        return baseDictionaryRepository.findByKey(key).orElseThrow(KeyNotFoundException::new);
    }

    public T addEntry(KeyValuePairRequestDto keyValuePairDto) {
        try {
            validate(keyValuePairDto.getKey());
            T baseDictionary = dictionaryClass.getDeclaredConstructor().newInstance();
            baseDictionary.setKey(keyValuePairDto.getKey());
            baseDictionary.setValue(keyValuePairDto.getValue());
            return baseDictionaryRepository.save(baseDictionary);
        } catch (Throwable e) {
            throw new DictionaryException(e.getMessage());
        }
    }

    protected void validate(String key) {
        if (!validation.validate(key)) {
            throw new ValidationException(validation.getRequirements());
        }
    }

    @Transactional
    public void getDictionaryAsXML(OutputStream outputStream) {
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream)) {
            writeString(bufferedOutputStream, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<dictionary>\n");

            try (Stream<T> stream = baseDictionaryRepository.streamAllEntities()) {
                XmlMapper xmlMapper = new XmlMapper();
                stream.forEach(entry -> {
                    try {
                        String xmlEntry = xmlMapper.writeValueAsString(entry);
                        writeString(bufferedOutputStream, xmlEntry);
                    } catch (IOException e) {
                        throw new DictionaryException(e.getMessage());
                    }
                });
            }

            writeString(bufferedOutputStream, "</dictionary>");
        } catch (IOException e) {
            throw new DictionaryException(e.getMessage());
        }
    }

    private void writeString(BufferedOutputStream bufferedOutputStream, String data) throws IOException {
        bufferedOutputStream.write(data.getBytes(StandardCharsets.UTF_8));
        bufferedOutputStream.write("\n".getBytes(StandardCharsets.UTF_8));
    }
}



