package repository;

import pojo.KeyValuePair;
import exception.AddEntryException;
import exception.RemoveEntryException;
import pojo.KeyValuePairGroup;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class BaseFileDictionary implements DictionaryRepository {

    protected final Path dectionaryPath;

    public BaseFileDictionary(Path dectionaryPath) {
        this.dectionaryPath = dectionaryPath;
    }

    @Override
    public List<KeyValuePair> findAll() {
        try (Stream<String> stream = Files.lines(dectionaryPath, Charset.defaultCharset())) {
            return stream.map(line -> {
                        String[] parts = line.split(" ", 2);
                        return new KeyValuePair(parts[0], parts[1]);
                    })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean removeEntryByKey(String key) {
        boolean found = false;
        try {
            File tempFile = new File("temp.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            BufferedReader reader = new BufferedReader(new FileReader(dectionaryPath.toFile()));
            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                if (!currentLine.startsWith(key)) {
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
        } catch (IOException e) {
            throw new RemoveEntryException(e.getMessage());
        }
    }

    @Override
    public Optional<String> searchEntryByKey(String key) {
        try (Stream<String> stream = Files.lines(dectionaryPath, Charset.defaultCharset())) {
            return stream.filter(s -> s.startsWith(key + " ")).findFirst();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void addEntry(String key, String value) {
        String entry = key + " " + value + System.lineSeparator();
        try {
            Files.write(dectionaryPath, entry.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new AddEntryException();
        }
    }

    @Override
    public KeyValuePairGroup pagination(int page, int size) {
        KeyValuePairGroup result = new KeyValuePairGroup();
        List<KeyValuePair> items = new ArrayList<>();
        int totalPages = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(dectionaryPath.toFile()))) {
            int totalLines = (int) Files.lines(dectionaryPath, Charset.defaultCharset()).count();
            totalPages = (int) Math.ceil((double) totalLines / size);
            if (page < 0 || page >= totalPages) {
                return result;
            }
            int startLine = page * size;
            String line;
            int currentLine = 0;
            while ((line = reader.readLine()) != null) {

                if (currentLine < startLine) {
                    currentLine++;
                    continue;
                }

                if (currentLine >= startLine + size) {
                    break;
                }

                String[] parts = line.split(" ", 2);
                KeyValuePair item = new KeyValuePair(parts[0], parts[1]);
                items.add(item);
                currentLine++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        result.setDictionary(items);
        result.setCount(totalPages);
        return result;
    }
}
