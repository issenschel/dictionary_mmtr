package com.example.dictionary_mmtr.service;

import com.example.dictionary_mmtr.entity.DictionaryEntry;
import com.example.dictionary_mmtr.entity.DictionaryType;
import com.example.dictionary_mmtr.repository.DictionaryEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class DictionaryEntryService {

    private final DictionaryEntryRepository dictionaryEntryRepository;

    public Optional<DictionaryEntry> findByProcessedKeyAndDictionaryType(String key, DictionaryType dictionaryType){
        return dictionaryEntryRepository.findByProcessedKeyAndDictionaryType(key, dictionaryType);
    }

    @Transactional
    public void incrementSearchCount(Integer entryId) {
        dictionaryEntryRepository.incrementSearchCount(entryId);
    }

    public Page<DictionaryEntry> findByDictionaryType(DictionaryType dictionaryType, Pageable pageable) {
        return dictionaryEntryRepository.findByDictionaryType(dictionaryType, pageable);
    }

    public Stream<DictionaryEntry> streamByDictionaryType(DictionaryType dictionaryType){
        return dictionaryEntryRepository.streamByDictionaryType(dictionaryType);
    }

    public Page<DictionaryEntry> findByDictionaryTypeAndFilters(DictionaryType dictionaryType, Pageable pageable, String keyFilter, String valueFilter) {
        return dictionaryEntryRepository.findByDictionaryTypeAndFilters(dictionaryType, pageable, keyFilter, valueFilter);
    }

    public Page<DictionaryEntry> findAllDictionaryEntriesAcrossAllDictionaries(List<DictionaryType> dictionaryTypes, Pageable pageable, String keyFilter, String valueFilter) {
        return dictionaryEntryRepository.findAllDictionaryEntriesAcrossAllDictionaries(dictionaryTypes, pageable, keyFilter, valueFilter);
    }


    public DictionaryEntry createDictionaryEntry(DictionaryType dictionaryType, String key, String processedKey) {
        DictionaryEntry dictionaryEntry = new DictionaryEntry();
        dictionaryEntry.setDictionaryType(dictionaryType);
        dictionaryEntry.setKey(key);
        dictionaryEntry.setProcessedKey(processedKey);
        return dictionaryEntryRepository.save(dictionaryEntry);
    }

    public void deleteDictionaryEntry(DictionaryEntry dictionaryEntry){
        dictionaryEntryRepository.delete(dictionaryEntry);
    }
}
