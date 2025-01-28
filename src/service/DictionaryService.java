package service;

import org.w3c.dom.Document;
import pojo.KeyValuePair;
import pojo.KeyValuePairGroup;

import javax.xml.transform.Transformer;
import java.util.List;

public interface DictionaryService {
   List<KeyValuePair> findAll();
   String removeEntryByKey(String key);
   KeyValuePair searchEntryByKey(String key);
   KeyValuePair addEntry(String key, String value);
   KeyValuePairGroup getPage(int page, int size);
   Document getDictionaryAsXML();
}
