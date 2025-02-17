package com.example.dictionary_mmtr.service;

import com.example.dictionary_mmtr.dto.DictionaryTypeDto;
import com.example.dictionary_mmtr.dto.KeyValuePairGroupDto;
import com.example.dictionary_mmtr.dto.KeyValuePairRequestDto;
import com.example.dictionary_mmtr.dto.ResponseDto;
import com.example.dictionary_mmtr.entity.BaseDictionary;
import com.example.dictionary_mmtr.entity.DictionaryType;
import com.example.dictionary_mmtr.exception.*;
import com.example.dictionary_mmtr.repository.DictionaryRepository;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class BaseDictionaryService {

    private final DictionaryRepository dictionaryRepository;
    private final MessageSource messageSource;
    private final DictionaryTypeService dictionaryTypeService;

    @Transactional
    public DictionaryType createDictionaryTypeWithTable(DictionaryTypeDto dictionaryTypeDto) {
        dictionaryTypeService.findDictionaryTypeByName(dictionaryTypeDto.getTableName()).ifPresent(t -> {
            throw new DictionaryFoundException();
        });

        dictionaryRepository.createDictionaryTables(dictionaryTypeDto.getTableName());
        return dictionaryTypeService.createDictionaryType(dictionaryTypeDto);
    }

    public List<DictionaryType> getActiveDictionaryTypes() {
        return dictionaryTypeService.getActiveDictionaryTypes();
    }

    @Transactional
    public ResponseDto deleteDictionaryType(String dictionaryTypeName) {
        ResponseDto responseDto = dictionaryTypeService.deleteDictionaryType(dictionaryTypeName);
        responseDto.setMessage(messageSource.getMessage(responseDto.getMessage(), null, LocaleContextHolder.getLocale()));
        return responseDto;
    }


    public KeyValuePairGroupDto getDictionaryEntries(String tableName, int page, int size, String keyFilter, String valueFilter, boolean searchAllDictionaries) {
        validateDictionaryType(tableName);

        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<BaseDictionary> dictionaryEntries;

        if (searchAllDictionaries) {
            dictionaryEntries = dictionaryRepository.findAllDictionaryEntriesAcrossAllDictionaries(getActiveDictionaryTypes(),pageRequest, keyFilter, valueFilter);
        } else {
            dictionaryEntries = dictionaryRepository.findAllDictionaryEntries(tableName, pageRequest, keyFilter, valueFilter);
        }

        KeyValuePairGroupDto keyValuePairGroupDto = new KeyValuePairGroupDto();
        keyValuePairGroupDto.setDictionary(dictionaryEntries.getContent());
        keyValuePairGroupDto.setCount(dictionaryEntries.getTotalPages());

        return keyValuePairGroupDto;
    }

    public BaseDictionary findDictionaryEntryByKey(String tableName, String key) {
        DictionaryType dictionaryType = validateDictionaryType(tableName);
        validateKey(dictionaryType, key);

        return dictionaryRepository.findDictionaryEntryByKey(key, dictionaryType.getFilterSQL())
                .orElseThrow(KeyNotFoundException::new);
    }

    @Transactional
    public BaseDictionary addDictionaryEntry(String tableName, KeyValuePairRequestDto keyValuePairDto) {
        DictionaryType dictionaryType = validateDictionaryType(tableName);
        validateKey(dictionaryType, keyValuePairDto.getKey());

        dictionaryRepository.findDictionaryEntryByKey(keyValuePairDto.getKey(), dictionaryType.getFilterSQL())
                .ifPresent(entry -> {
                    throw new KeyFoundException();
                });

        return dictionaryRepository.addDictionaryEntry(tableName, keyValuePairDto.getKey(), keyValuePairDto.getValue());
    }

    @Transactional
    public ResponseDto removeDictionaryEntryByKey(String tableName, String key) {
        BaseDictionary entry = findDictionaryEntryByKey(tableName, key);
        dictionaryRepository.deleteDictionaryEntry(tableName, entry);
        return new ResponseDto(messageSource.getMessage("success.entry.removed", null, LocaleContextHolder.getLocale()));
    }


    private DictionaryType validateDictionaryType(String tableName) {
        DictionaryType dictionaryType = dictionaryTypeService.findDictionaryTypeByName(tableName)
                .orElseThrow(DictionaryNotFoundException::new);

        if (dictionaryType.getDeleted()) {
            throw new DictionaryNotFoundException();
        }

        return dictionaryType;
    }

    private void validateKey(DictionaryType dictionaryType, String key) {
        if (!key.matches(dictionaryType.getRegexPattern())) {
            throw new ValidationException(dictionaryType.getRegexPattern());
        }
    }


    @Transactional
    public void exportDictionaryToXml(String tableName, OutputStream outputStream) {
        validateDictionaryType(tableName);

        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream)) {
            writeString(bufferedOutputStream, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<dictionary>\n");

            try (Stream<BaseDictionary> stream = dictionaryRepository.streamAllDictionaryEntries(tableName)) {
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