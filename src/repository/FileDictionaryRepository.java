package repository;

import enums.DectionaryType;
import exception.AddEntryException;
import exception.KeyNotFoundException;
import exception.RemoveEntryException;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.stream.Stream;

public class FileDictionaryRepository implements DictionaryRepository{

    private final DectionaryType dictionary;

    public FileDictionaryRepository(DectionaryType dectionaryType) {
        this.dictionary = dectionaryType;
    }

    public void displayDictionary(){
        try (Stream<String> stream = Files.lines(Paths.get(dictionary.getDescription()), Charset.defaultCharset())) {
            stream.forEach(System.out::println);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean removeEntryByKey(String key){
        boolean found = false;
        try {
            File tempFile = new File("temp.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            BufferedReader reader = new BufferedReader(new FileReader(dictionary.getDescription()));
            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                if (!currentLine.startsWith(key)) {
                    writer.write(currentLine + System.lineSeparator());
                    found = true;
                }
            }

            reader.close();
            writer.close();

            File oldFile = new File(dictionary.getDescription());
            oldFile.delete();
            tempFile.renameTo(oldFile);
            return found;
        } catch (IOException e) {
            throw new RemoveEntryException(e.getMessage());
        }
    }

    public String searchEntryByKey(String key){
        try (Stream<String> stream = Files.lines(Paths.get(dictionary.getDescription()), Charset.defaultCharset())) {
            return stream.filter(s -> s.startsWith(key + " ")).findFirst().orElseThrow(KeyNotFoundException::new);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void addEntry(String key, String value){
        String entry = key + " " + value + System.lineSeparator();
        try {
            Files.write(Paths.get(dictionary.getDescription()), entry.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new AddEntryException();
        }
    }
}
