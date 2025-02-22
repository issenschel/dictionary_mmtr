package com.example.dictionary_mmtr.service;

import com.example.dictionary_mmtr.dto.*;
import com.example.dictionary_mmtr.entity.DictionaryEntry;
import com.example.dictionary_mmtr.entity.DictionaryType;
import com.example.dictionary_mmtr.exception.KeyFoundException;
import com.example.dictionary_mmtr.exception.KeyNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.OutputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BaseDictionaryService {
    private final DictionaryEntryService dictionaryEntryService;
    private final DictionaryTypeService dictionaryTypeService;
    private final DictionaryValueService dictionaryValueService;
    private final DictionaryValidationService validationService;
    private final DictionaryMapper dictionaryMapper;
    private final DictionaryExportService exportService;
    private final MessageSource messageSource;

    public KeyValuePairGroupDto getDictionaryEntries(DictionaryQueryDto query) {
        PageRequest pageRequest = PageRequest.of(query.getPage() - 1, query.getSize());
        Page<DictionaryEntry> entriesPage;
        if (query.isSearchAll()) {
            List<DictionaryType> activeDictionaryTypes = dictionaryTypeService.getActiveDictionaryTypes();
            entriesPage = dictionaryEntryService.findAllDictionaryEntriesAcrossAllDictionaries(
                    activeDictionaryTypes, pageRequest, query.getKeyFilter(), query.getValueFilter());
        } else {
            DictionaryType dictionaryType = validationService.validateDictionaryType(query.getDictionaryType());
            entriesPage = dictionaryEntryService.findByDictionaryTypeAndFilters(
                    dictionaryType, pageRequest, query.getKeyFilter(), query.getValueFilter());
        }

        return createKeyValuePairGroupDto(entriesPage, query.isUseLegacyFormat());
    }

    private KeyValuePairGroupDto createKeyValuePairGroupDto(Page<DictionaryEntry> entriesPage, boolean useLegacyFormat) {
        KeyValuePairGroupDto keyValuePairGroupDto = new KeyValuePairGroupDto();
        List<KeyValuesDto> dictionaryEntries = entriesPage.getContent().stream()
                .map(entry -> dictionaryMapper.convertToDictionaryDto(entry, useLegacyFormat))
                .collect(Collectors.toList());

        keyValuePairGroupDto.setDictionary(dictionaryEntries);
        keyValuePairGroupDto.setCount(entriesPage.getTotalPages());
        return keyValuePairGroupDto;
    }

    public KeyValuesDto findDictionaryEntryByKey(String tableName, String key) {
        DictionaryEntry dictionaryEntry = findValidatedDictionaryEntry(tableName, key).orElseThrow(KeyNotFoundException::new);
        return dictionaryMapper.convertToDictionaryDto(dictionaryEntry, false);
    }

    @Transactional
    public KeyValuePairDto addDictionaryEntry(String tableName, KeyValuePairRequestDto keyValuePairDto) {
        DictionaryType dictionaryType = validationService.validateDictionaryType(tableName);
        validationService.validateKey(dictionaryType, keyValuePairDto.getKey());
        String processedKey = validationService.processKey(keyValuePairDto.getKey(), dictionaryType);

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
        DictionaryType dictionaryType = validationService.validateDictionaryType(tableName);
        exportService.exportDictionaryToXml(dictionaryType, outputStream);
    }

    private Optional<DictionaryEntry> findValidatedDictionaryEntry(String tableName, String key) {
        DictionaryType dictionaryType = validationService.validateDictionaryType(tableName);
        validationService.validateKey(dictionaryType, key);
        String processedKey = validationService.processKey(key, dictionaryType);
        return dictionaryEntryService.findByProcessedKeyAndDictionaryType(processedKey, dictionaryType);
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
}