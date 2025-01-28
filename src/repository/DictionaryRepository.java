package repository;

import pojo.KeyValuePair;
import pojo.KeyValuePairGroup;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface DictionaryRepository {
    List<KeyValuePair> findAll() throws IOException;
    KeyValuePairGroup pagination(int page, int size) throws IOException;
    boolean removeEntryByKey(String key) throws IOException;
    Optional<KeyValuePair> searchEntryByKey(String key) throws IOException;
    KeyValuePair addEntry(KeyValuePair keyValuePair) throws IOException;
}
