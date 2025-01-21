package service;

public interface DictionaryService {
   void displayDictionary();
   void removeEntryByKey(String key);
   void searchEntryByKey(String key);
   void addEntry(String key, String value);
}
