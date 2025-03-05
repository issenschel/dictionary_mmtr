package com.example.dictionary_mmtr.service;

import com.example.dictionary_mmtr.dto.KeyValuePairDto;
import com.example.dictionary_mmtr.entity.DictionaryEntry;
import com.example.dictionary_mmtr.entity.DictionaryType;
import com.example.dictionary_mmtr.exception.DictionaryException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class DictionaryExportService {
    private final DictionaryEntryService dictionaryEntryService;

    public void exportDictionaryToXml(DictionaryType dictionaryType, OutputStream outputStream) {
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream)) {
            writeString(bufferedOutputStream, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<dictionary>\n");

            try (Stream<DictionaryEntry> stream = dictionaryEntryService.streamByDictionaryType(dictionaryType)) {
                XmlMapper xmlMapper = new XmlMapper();
                stream.forEach(entry -> entry.getValues().forEach(value -> {
                    try {
                        String xmlEntry = xmlMapper.writeValueAsString(new KeyValuePairDto(entry.getKey(), value.getValue(), dictionaryType.getName()));
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
