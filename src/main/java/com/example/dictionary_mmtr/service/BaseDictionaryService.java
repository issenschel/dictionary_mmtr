package com.example.dictionary_mmtr.service;

import com.example.dictionary_mmtr.dto.*;
import com.example.dictionary_mmtr.entity.DictionaryEntry;
import com.example.dictionary_mmtr.entity.DictionaryType;
import com.example.dictionary_mmtr.entity.DictionaryValue;
import com.example.dictionary_mmtr.exception.*;
import com.example.dictionary_mmtr.validation.Validation;
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
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class BaseDictionaryService {

    private final DictionaryEntryService dictionaryEntryService;
    private final DictionaryValueService dictionaryValueService;
    private final DictionaryTypeService dictionaryTypeService;
    private final MessageSource messageSource;

    public KeyValuePairGroupDto getDictionaryEntries(String tableName, int page, int size) {
        DictionaryType dictionaryType = validateDictionaryType(tableName);
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<DictionaryEntry> entriesPage = dictionaryEntryService.findByDictionaryType(dictionaryType, pageRequest);

        KeyValuePairGroupDto keyValuePairGroupDto = new KeyValuePairGroupDto();
        keyValuePairGroupDto.setDictionary(entriesPage.getContent().stream()
                .map(this::convertToDictionaryDto)
                .collect(Collectors.toList()));
        keyValuePairGroupDto.setCount(entriesPage.getTotalPages());
        return keyValuePairGroupDto;
    }

    public DictionaryDto findDictionaryEntryByKey(String tableName, String key) {
        DictionaryEntry dictionaryEntry = findValidatedDictionaryEntry(tableName, key).orElseThrow(KeyNotFoundException::new);
        return convertToDictionaryDto(dictionaryEntry);
    }

    @Transactional
    public KeyValuePairDto addDictionaryEntry(String tableName, KeyValuePairRequestDto keyValuePairDto) {
        DictionaryType dictionaryType = validateDictionaryType(tableName);
        validateKey(dictionaryType, keyValuePairDto.getKey());
        String processedKey = processKey(keyValuePairDto.getKey(), dictionaryType);

        Optional<DictionaryEntry> optionalEntry = findValidatedDictionaryEntry(tableName, processedKey);

        if (optionalEntry.isPresent()) {
            handleExistingEntry(optionalEntry.get(), keyValuePairDto.getValue());
        } else {
            createNewEntry(dictionaryType, keyValuePairDto, processedKey);
        }

        return new KeyValuePairDto(keyValuePairDto.getKey(), keyValuePairDto.getValue());
    }

    @Transactional
    public ResponseDto removeDictionaryEntryByKey(String tableName, String key) {
        DictionaryEntry dictionaryEntry = findValidatedDictionaryEntry(tableName, key).orElseThrow(KeyNotFoundException::new);
        dictionaryEntryService.deleteDictionaryEntry(dictionaryEntry);
        return new ResponseDto(messageSource.getMessage("success.entry.removed", null, LocaleContextHolder.getLocale()));
    }

    @Transactional
    public void exportDictionaryToXml(String tableName, OutputStream outputStream) {
        DictionaryType dictionaryType = validateDictionaryType(tableName);

        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream)) {
            writeString(bufferedOutputStream, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<dictionary>\n");

            try (Stream<DictionaryEntry> stream = dictionaryEntryService.streamByDictionaryType(dictionaryType)) {
                XmlMapper xmlMapper = new XmlMapper();
                stream.forEach(entry -> entry.getValues().forEach(value -> {
                    try {
                        String xmlEntry = xmlMapper.writeValueAsString(new KeyValuePairDto(entry.getKey(), value.getValue()));
                        writeString(bufferedOutputStream, xmlEntry);
                    } catch (IOException e) {
                        throw new DictionaryException(e.getMessage());
                    }
                }));
            }

            writeString(bufferedOutputStream, "</dictionary>");
        } catch (IOException e) {
            throw new DictionaryException(e.getMessage());
        }
    }

    private Optional<DictionaryEntry> findValidatedDictionaryEntry(String tableName, String key) {
        DictionaryType dictionaryType = validateDictionaryType(tableName);
        validateKey(dictionaryType, key);
        String processedKey = processKey(key, dictionaryType);
        return dictionaryEntryService.findByProcessedKeyAndDictionaryType(processedKey, dictionaryType);
    }

    private DictionaryDto convertToDictionaryDto(DictionaryEntry entry) {
        List<String> values = entry.getValues().stream()
                .map(DictionaryValue::getValue)
                .collect(Collectors.toList());
        return new DictionaryDto(entry.getKey(), values);
    }

    private void handleExistingEntry(DictionaryEntry entry, String value) {
        if (entry.getValues().stream().anyMatch(e -> e.getValue().equals(value))) {
            throw new KeyFoundException();
        }
        dictionaryValueService.createDictionaryValue(entry, value);
    }

    private void createNewEntry(DictionaryType dictionaryType, KeyValuePairRequestDto keyValuePairDto, String processedKey) {
        DictionaryEntry dictionaryEntry = dictionaryEntryService.createDictionaryEntry(dictionaryType, keyValuePairDto.getKey(), processedKey);
        dictionaryValueService.createDictionaryValue(dictionaryEntry, keyValuePairDto.getValue());
    }

    private void writeString(BufferedOutputStream bufferedOutputStream, String data) throws IOException {
        bufferedOutputStream.write(data.getBytes(StandardCharsets.UTF_8));
        bufferedOutputStream.write("\n".getBytes(StandardCharsets.UTF_8));
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
        Validation validation = dictionaryType.getValidationType().getType().getValidation();
        if (!validation.validate(key)) {
            throw new ValidationException(validation.getRequirements());
        }
    }

    private String processKey(String key, DictionaryType dictionaryType) {
        Validation validation = dictionaryType.getValidationType().getType().getValidation();
        return validation.getKeyTransformer().apply(key);
    }
}