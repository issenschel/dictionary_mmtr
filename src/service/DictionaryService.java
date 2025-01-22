package service;

import dto.DictionaryDto;

import java.util.List;

public interface DictionaryService {
   List<DictionaryDto> findAll();
   void removeEntryByKey(String key);
   void searchEntryByKey(String key);
   void addEntry(String key, String value);
}
