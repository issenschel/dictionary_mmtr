package repository;

import exception.RemoveEntryException;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

public class BackspaceDictionaryRepository extends BaseFileDictionary {
    public BackspaceDictionaryRepository(Path dectionaryPath) {
        super(dectionaryPath);
    }

    @Override
    public boolean removeEntryByKey(String key) {
        boolean found = false;
        String processedKey = processKey(key);
        try {
            File tempFile = new File("temp.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            BufferedReader reader = new BufferedReader(new FileReader(dectionaryPath.toFile()));
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
        } catch (IOException e) {
            throw new RemoveEntryException(e.getMessage());
        }
    }

    @Override
    public Optional<String> searchEntryByKey(String key) {
        String processedKey = processKey(key);
        try (Stream<String> stream = Files.lines(dectionaryPath, Charset.defaultCharset())) {
            return stream.filter(s -> processKey(s.substring(0, s.indexOf(' '))).equals(processedKey)).findFirst();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
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
