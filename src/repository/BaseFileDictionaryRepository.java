package repository;

import pojo.KeyValuePair;
import pojo.KeyValuePairGroup;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BaseFileDictionaryRepository implements DictionaryRepository {

    private final Path dectionaryPath;
    private final Function<String, String> keyTransformer;

    public BaseFileDictionaryRepository(Path dectionaryPath, Function<String, String> keyTransformer) {
        this.dectionaryPath = dectionaryPath;
        this.keyTransformer = keyTransformer;
    }

    public List<KeyValuePair> findAll() throws IOException {
        try (Stream<String> stream = Files.lines(dectionaryPath, Charset.defaultCharset())) {
            return stream.map(line -> {
                        String[] parts = line.split(" ", 2);
                        return new KeyValuePair(parts[0], parts[1]);
                    }).collect(Collectors.toList());
        }
    }

    public boolean removeEntryByKey(String key) throws IOException {
        boolean found = false;
        String processedKey = this.keyTransformer.apply(key);
        File tempFile = new File("temp.txt");
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            BufferedReader reader = new BufferedReader(new FileReader(dectionaryPath.toFile()))) {

            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                String lineKey = currentLine.substring(0, currentLine.indexOf(' '));
                if (!this.keyTransformer.apply(lineKey).equals(processedKey)) {
                    writer.write(currentLine + System.lineSeparator());
                } else {
                    found = true;
                }
            }

            reader.close();
            writer.close();

            File oldFile = dectionaryPath.toFile();
            oldFile.delete();
            tempFile.renameTo(oldFile);
            return found;
        }
    }

    public Optional<KeyValuePair> searchEntryByKey(String key) throws IOException {
        String processedKey = this.keyTransformer.apply(key);
        try(Stream<String> stream = Files.lines(dectionaryPath, Charset.defaultCharset())) {
            return stream.filter(s -> this.keyTransformer.apply(s.substring(0, s.indexOf(' '))).equals(processedKey)).findFirst().map(s ->{
                String[] parts = s.split(" ", 2);
                return new KeyValuePair(parts[0], parts[1]);
            });
        }
    }

    public KeyValuePair addEntry(KeyValuePair keyValuePair) throws IOException {
        String entry = keyValuePair.getKey() + " " + keyValuePair.getValue() + System.lineSeparator();
        Files.write(dectionaryPath, entry.getBytes(), StandardOpenOption.APPEND);
        return keyValuePair;
    }

    public KeyValuePairGroup getPage(int page, int size) throws IOException {
        KeyValuePairGroup result = new KeyValuePairGroup();
        List<KeyValuePair> items;
        int totalPages;
        page -= 1;

        long totalLines = Files.lines(dectionaryPath, Charset.defaultCharset()).count();
        totalPages = (int) Math.ceil((double) totalLines / size);
        result.setCount(totalPages);

        if (page < 0 || page >= totalPages) {
            return result;
        }

        try (Stream<String> lines = Files.lines(dectionaryPath, Charset.defaultCharset())) {
            items = lines.skip(page * size)
                    .limit(size)
                    .map(line -> {
                        String[] parts = line.split(" ", 2);
                        return new KeyValuePair(parts[0], parts[1]);
                    })
                    .collect(Collectors.toList());
        }

        result.setDictionary(items);
        return result;
    }
}
