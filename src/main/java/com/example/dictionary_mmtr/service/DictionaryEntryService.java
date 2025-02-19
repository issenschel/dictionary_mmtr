package com.example.dictionary_mmtr.service;

import com.example.dictionary_mmtr.entity.DictionaryEntry;
import com.example.dictionary_mmtr.entity.DictionaryType;
import com.example.dictionary_mmtr.repository.DictionaryEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class DictionaryEntryService {

    private final DictionaryEntryRepository dictionaryEntryRepository;

    public Optional<DictionaryEntry> findByKeyAndDictionaryType(String key, DictionaryType dictionaryType){
        return dictionaryEntryRepository.findByKeyAndDictionaryType(key, dictionaryType);
    }

    public Page<DictionaryEntry> findByDictionaryType(DictionaryType dictionaryType, Pageable pageable) {
        return dictionaryEntryRepository.findByDictionaryType(dictionaryType, pageable);
    }

    public Stream<DictionaryEntry> streamByDictionaryType(DictionaryType dictionaryType){
        return dictionaryEntryRepository.streamByDictionaryType(dictionaryType);
    }

    public DictionaryEntry createDictionaryEntry(DictionaryType dictionaryType, String key) {
        DictionaryEntry dictionaryEntry = new DictionaryEntry();
        dictionaryEntry.setDictionaryType(dictionaryType);
        dictionaryEntry.setKey(key);
        return dictionaryEntryRepository.save(dictionaryEntry);
    }

    public void deleteDictionaryEntry(DictionaryEntry dictionaryEntry){
        dictionaryEntryRepository.delete(dictionaryEntry);
    }
}
