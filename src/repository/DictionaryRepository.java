package repository;

import dto.DictionaryDto;

import java.util.List;

public interface DictionaryRepository {
    List<DictionaryDto> findAll();
    boolean removeEntryByKey(String key);
    String searchEntryByKey(String key);
    void addEntry(String key, String value);
}
