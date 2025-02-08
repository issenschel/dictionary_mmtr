package com.example.dictionary_mmtr.repository;

import com.example.dictionary_mmtr.dto.KeyValuePairDto;
import com.example.dictionary_mmtr.dto.KeyValuePairGroupDto;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface DictionaryRepository {
    List<KeyValuePairDto> findAll() throws IOException;
    KeyValuePairGroupDto getPage(int page, int size) throws IOException;
    boolean removeEntryByKey(String key) throws IOException;
    Optional<KeyValuePairDto> searchEntryByKey(String key) throws IOException;
    KeyValuePairDto addEntry(KeyValuePairDto keyValuePairDto) throws IOException;
}
