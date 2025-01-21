package repository;

public interface DictionaryRepository {
    void displayDictionary();
    boolean removeEntryByKey(String key);
    String searchEntryByKey(String key);
    void addEntry(String key, String value);
}
