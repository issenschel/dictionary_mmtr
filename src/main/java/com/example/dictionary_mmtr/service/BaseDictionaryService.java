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
import org.springframework.data.domain.Sort;
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
    private final DictionaryCallbackService callbackService;

    public KeyValuePairGroupDto getDictionaryEntries(DictionaryQueryDto query) {
        PageRequest pageRequest = PageRequest.of(query.getPage() - 1, query.getSize(), Sort.by(Sort.Direction.DESC, "searchCount"));
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

        return createKeyValuePairGroupDto(entriesPage);
    }

    private KeyValuePairGroupDto createKeyValuePairGroupDto(Page<DictionaryEntry> entriesPage) {
        KeyValuePairGroupDto keyValuePairGroupDto = new KeyValuePairGroupDto();
        List<KeyValuePairDto> dictionaryEntries = entriesPage.getContent().stream()
                .flatMap(entry -> dictionaryMapper.convertToDictionaryDto(entry).stream())
                .collect(Collectors.toList());

        keyValuePairGroupDto.setDictionary(dictionaryEntries);
        keyValuePairGroupDto.setCount(entriesPage.getTotalPages());
        return keyValuePairGroupDto;
    }

    public List<KeyValuePairDto> findDictionaryEntryByKey(String tableName, String key) {
        DictionaryEntry dictionaryEntry = findValidatedDictionaryEntry(tableName, key).orElseThrow(KeyNotFoundException::new);
        dictionaryEntryService.incrementSearchCount(dictionaryEntry.getId());
        return dictionaryMapper.convertToDictionaryDto(dictionaryEntry);
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

        callbackService.notifySubscribers(dictionaryType, "entry_added", keyValuePairDto);
        return new KeyValuePairDto(keyValuePairDto.getKey(), keyValuePairDto.getValue(), dictionaryType.getName());
    }

    @Transactional
    public ResponseDto removeDictionaryEntryByKey(String tableName, String key) {
        DictionaryEntry dictionaryEntry = findValidatedDictionaryEntry(tableName, key).orElseThrow(KeyNotFoundException::new);
        dictionaryEntryService.deleteDictionaryEntry(dictionaryEntry);
        callbackService.notifySubscribers(dictionaryEntry.getDictionaryType(), "entry_removed", dictionaryEntry);
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