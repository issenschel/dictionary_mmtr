package com.example.dictionary_mmtr.repository;

import com.example.dictionary_mmtr.dto.KeyValuePairDto;
import com.example.dictionary_mmtr.dto.KeyValuePairGroupDto;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

public interface DictionaryRepository {
    Stream<KeyValuePairDto> findAll() throws IOException;

    KeyValuePairGroupDto getPage(int page, int size) throws IOException;

    boolean removeEntryByKey(String key) throws IOException;

    Optional<KeyValuePairDto> searchEntryByKey(String key) throws IOException;

    KeyValuePairDto addEntry(KeyValuePairDto keyValuePairDto) throws IOException;
}
