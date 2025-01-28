package service;

import pojo.KeyValuePair;
import pojo.KeyValuePairGroup;

import java.util.List;

public interface DictionaryService {
   List<KeyValuePair> findAll();
   void removeEntryByKey(String key);
   void searchEntryByKey(String key);
   void addEntry(String key, String value);
   KeyValuePairGroup pagination(int page, int size);
}
