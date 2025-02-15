package com.example.dictionary_mmtr.service;

import com.example.dictionary_mmtr.dto.KeyValuePairDto;
import com.example.dictionary_mmtr.dto.KeyValuePairGroupDto;
import com.example.dictionary_mmtr.dto.KeyValuePairRequestDto;
import com.example.dictionary_mmtr.dto.ResponseDto;
import com.example.dictionary_mmtr.entity.BaseDictionary;

import java.io.IOException;
import java.io.OutputStream;

public interface DictionaryService {

    ResponseDto removeEntryByKey(String key);

    BaseDictionary searchEntryByKey(String key);

    BaseDictionary addEntry(KeyValuePairRequestDto keyValuePairRequestDto);

    KeyValuePairGroupDto getPage(int page, int size);

    void getDictionaryAsXML(OutputStream outputStream) throws IOException;
}
