package repository;

import pojo.KeyValuePair;
import pojo.KeyValuePairGroup;

import java.util.List;
import java.util.Optional;

public interface DictionaryRepository {
    List<KeyValuePair> findAll();
    KeyValuePairGroup pagination(int page, int size);
    boolean removeEntryByKey(String key);
    Optional<String> searchEntryByKey(String key);
    void addEntry(String key, String value);
}
