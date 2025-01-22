package repository;

import dto.DictionaryDto;
import exception.AddEntryException;
import exception.KeyNotFoundException;
import exception.RemoveEntryException;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileDictionaryRepository implements DictionaryRepository {

    private final Path dectionaryPath;

    public FileDictionaryRepository(Path dectionaryPath) {
        this.dectionaryPath = dectionaryPath;
    }

    public List<DictionaryDto> findAll() {
        try (Stream<String> stream = Files.lines(dectionaryPath, Charset.defaultCharset())) {
            return stream.map(line -> {
                        String[] parts = line.split(" ", 2);
                        return new DictionaryDto(parts[0], parts[1]);
                    })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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

    public String searchEntryByKey(String key) {
        try (Stream<String> stream = Files.lines(dectionaryPath, Charset.defaultCharset())) {
            return stream.filter(s -> s.startsWith(key + " ")).findFirst().orElseThrow(KeyNotFoundException::new);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void addEntry(String key, String value) {
        String entry = key + " " + value + System.lineSeparator();
        try {
            Files.write(dectionaryPath, entry.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new AddEntryException();
        }
    }
}
