package com.example.dictionary_mmtr.repository;

import com.example.dictionary_mmtr.dto.KeyValuePairDto;
import com.example.dictionary_mmtr.dto.KeyValuePairGroupDto;
import com.example.dictionary_mmtr.validation.Validation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class BaseFileDictionaryRepository implements DictionaryRepository {

    private final Path dictionaryPath;
    private final Validation validation;

    @Value("${temp.file.name}")
    private String tempFileName;

    public BaseFileDictionaryRepository(Path dictionaryPath, Validation validation) {
        this.dictionaryPath = dictionaryPath;
        this.validation = validation;
    }

    @Override
    public Stream<KeyValuePairDto> findAll() throws IOException {
        return Files.lines(dictionaryPath, Charset.defaultCharset())
                .map(line -> {
                    String[] parts = line.split(" ", 2);
                    return new KeyValuePairDto(parts[0], parts[1]);
                });
    }

    public boolean removeEntryByKey(String key) throws IOException {
        boolean found = false;
        String processedKey = validation.getKeyTransformer().apply(key);
        File tempFile = new File(tempFileName);
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            BufferedReader reader = new BufferedReader(new FileReader(dictionaryPath.toFile()))) {

            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                String lineKey = currentLine.substring(0, currentLine.indexOf(' '));
                if (!validation.getKeyTransformer().apply(lineKey).equals(processedKey)) {
                    writer.write(currentLine + System.lineSeparator());
                } else {
                    found = true;
                }
            }

            reader.close();
            writer.close();

            File oldFile = dictionaryPath.toFile();
            oldFile.delete();
            tempFile.renameTo(oldFile);
            return found;
        }
    }

    public Optional<KeyValuePairDto> searchEntryByKey(String key) throws IOException {
        String processedKey = validation.getKeyTransformer().apply(key);
        try(Stream<String> stream = Files.lines(dictionaryPath, Charset.defaultCharset())) {
            return stream.filter(s -> validation.getKeyTransformer().apply(s.substring(0, s.indexOf(' '))).equals(processedKey)).findFirst().map(s ->{
                String[] parts = s.split(" ", 2);
                return new KeyValuePairDto(parts[0], parts[1]);
            });
        }
    }

    public KeyValuePairDto addEntry(KeyValuePairDto keyValuePairDto) throws IOException {
        String entry = keyValuePairDto.getKey() + " " + keyValuePairDto.getValue() + System.lineSeparator();
        Files.write(dictionaryPath, entry.getBytes(), StandardOpenOption.APPEND);
        return keyValuePairDto;
    }

    public KeyValuePairGroupDto getPage(int page, int size) throws IOException {
        KeyValuePairGroupDto result = new KeyValuePairGroupDto();
        List<KeyValuePairDto> items;
        int totalPages;
        page -= 1;

        long totalLines = Files.lines(dictionaryPath, Charset.defaultCharset()).count();
        totalPages = (int) Math.ceil((double) totalLines / size);
        result.setCount(totalPages);

        if (page < 0 || page >= totalPages) {
            return result;
        }

        try (Stream<String> lines = Files.lines(dictionaryPath, Charset.defaultCharset())) {
            items = lines.skip(page * size)
                    .limit(size)
                    .map(line -> {
                        String[] parts = line.split(" ", 2);
                        return new KeyValuePairDto(parts[0], parts[1]);
                    })
                    .collect(Collectors.toList());
        }

        result.setDictionary(items);
        return result;
    }
}
