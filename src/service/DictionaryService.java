package service;

import pojo.KeyValuePair;
import pojo.KeyValuePairGroup;

import java.util.List;

public interface DictionaryService {
   List<KeyValuePair> findAll();
   String removeEntryByKey(String key);
   KeyValuePair searchEntryByKey(String key);
   KeyValuePair addEntry(String key, String value);
   KeyValuePairGroup pagination(int page, int size);
}
