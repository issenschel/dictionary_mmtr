package com.example.dictionary_mmtr.service;

import com.example.dictionary_mmtr.dto.*;
import com.example.dictionary_mmtr.entity.DictionaryEntry;
import com.example.dictionary_mmtr.entity.DictionaryType;
import com.example.dictionary_mmtr.entity.DictionaryValue;
import com.example.dictionary_mmtr.exception.*;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        List<DictionaryDto> dictionaryDtos = entriesPage.getContent().stream()
                .map(entry -> new DictionaryDto(entry.getKey(), entry.getValues().stream()
                        .map(DictionaryValue::getValue)
                        .collect(Collectors.toList())))
                .collect(Collectors.toList());

        keyValuePairGroupDto.setDictionary(dictionaryDtos);
        keyValuePairGroupDto.setCount(entriesPage.getTotalPages());
        return keyValuePairGroupDto;
    }


    public DictionaryDto findDictionaryEntryByKey(String tableName, String key) {
        DictionaryType dictionaryType = validateDictionaryType(tableName);
        validateKey(dictionaryType, key);

        DictionaryEntry dictionaryEntry = dictionaryEntryService.findByKeyAndDictionaryType(key, dictionaryType).orElseThrow(KeyNotFoundException::new);

        List<String> values = dictionaryEntry.getValues().stream().map(DictionaryValue::getValue).collect(Collectors.toList());

        return new DictionaryDto(key, values);
    }

    @Transactional
    public KeyValuePairDto addDictionaryEntry(String tableName, KeyValuePairRequestDto keyValuePairDto) {
        DictionaryType dictionaryType = validateDictionaryType(tableName);
        validateKey(dictionaryType, keyValuePairDto.getKey());

        Optional<DictionaryEntry> optionalEntry = dictionaryEntryService.findByKeyAndDictionaryType(keyValuePairDto.getKey(), dictionaryType);

        if (optionalEntry.isPresent()) {
            handleExistingEntry(optionalEntry.get(), keyValuePairDto.getValue());
        } else {
            createNewEntry(dictionaryType, keyValuePairDto);
        }

        return new KeyValuePairDto(keyValuePairDto.getKey(), keyValuePairDto.getValue());
    }

    private void handleExistingEntry(DictionaryEntry entry, String value) {
        entry.getValues().stream().filter(e -> e.getValue().equals(value)).findFirst().ifPresent(e -> {
            throw new KeyFoundException();
        });
        dictionaryValueService.createDictionaryValue(entry, value);
    }

    private void createNewEntry(DictionaryType dictionaryType, KeyValuePairRequestDto keyValuePairDto) {
        DictionaryEntry dictionaryEntry = dictionaryEntryService.createDictionaryEntry(dictionaryType, keyValuePairDto.getKey());
        dictionaryValueService.createDictionaryValue(dictionaryEntry, keyValuePairDto.getValue());
    }


    @Transactional
    public ResponseDto removeDictionaryEntryByKey(String tableName, String key) {
        DictionaryType dictionaryType = validateDictionaryType(tableName);
        validateKey(dictionaryType, key);

        DictionaryEntry dictionaryEntry = dictionaryEntryService.findByKeyAndDictionaryType(key, dictionaryType).orElseThrow(KeyNotFoundException::new);
        dictionaryEntryService.deleteDictionaryEntry(dictionaryEntry);

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
        DictionaryType dictionaryType = validateDictionaryType(tableName);

        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream)) {
            writeString(bufferedOutputStream, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<dictionary>\n");

            try (Stream<DictionaryEntry> stream = dictionaryEntryService.streamByDictionaryType(dictionaryType)) {
                XmlMapper xmlMapper = new XmlMapper();
                stream.forEach(dictionaryValues -> dictionaryValues.getValues().stream().forEach(value -> {
                    try {
                        KeyValuePairDto keyValuePairDto = new KeyValuePairDto(dictionaryValues.getKey(), value.getValue());
                        String xmlEntry = xmlMapper.writeValueAsString(keyValuePairDto);
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

    private void writeString(BufferedOutputStream bufferedOutputStream, String data) throws IOException {
        bufferedOutputStream.write(data.getBytes(StandardCharsets.UTF_8));
        bufferedOutputStream.write("\n".getBytes(StandardCharsets.UTF_8));
    }


}