package com.example.dictionary_mmtr.service;

import com.example.dictionary_mmtr.dto.KeyValuePairDto;
import com.example.dictionary_mmtr.dto.KeyValuePairGroupDto;
import com.example.dictionary_mmtr.dto.ResponseDto;

import java.io.IOException;
import java.io.OutputStream;

public interface DictionaryService {

    ResponseDto removeEntryByKey(String key);

    KeyValuePairDto searchEntryByKey(String key);

    KeyValuePairDto addEntry(String key, String value);

    KeyValuePairGroupDto getPage(int page, int size);

    void getDictionaryAsXML(OutputStream outputStream) throws IOException;
}
