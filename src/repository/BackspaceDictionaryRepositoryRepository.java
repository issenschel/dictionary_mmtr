package repository;

import pojo.KeyValuePair;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

public class BackspaceDictionaryRepositoryRepository extends BaseFileDictionaryRepository {
    public BackspaceDictionaryRepositoryRepository(Path dectionaryPath) {
        super(dectionaryPath);
    }

    @Override
    public boolean removeEntryByKey(String key) throws IOException {
        boolean found = false;
        String processedKey = processKey(key);
        File tempFile = new File("temp.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
             BufferedReader reader = new BufferedReader(new FileReader(dectionaryPath.toFile()))) {
            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                if (!processKey(currentLine.substring(0, currentLine.indexOf(' '))).equals(processedKey)) {
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

    @Override
    public Optional<KeyValuePair> searchEntryByKey(String key) throws IOException {
        String processedKey = processKey(key);
        try (Stream<String> stream = Files.lines(dectionaryPath, Charset.defaultCharset())) {
            return stream.filter(s -> processKey(s.substring(0, s.indexOf(' '))).equals(processedKey)).findFirst().map(s -> {
                String[] split = s.split(" ");
                return new KeyValuePair(split[0], split[1]);
            });
        }
    }

    private String processKey(String key) {
        StringBuilder processedKey = new StringBuilder();
        for (char ch : key.toCharArray()) {
            if (ch == '#') {
                if (processedKey.length() > 0) {
                    processedKey.deleteCharAt(processedKey.length() - 1);
                }
            } else {
                processedKey.append(ch);
            }
        }
        return processedKey.toString();
    }
}
