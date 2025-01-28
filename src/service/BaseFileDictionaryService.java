package service;

import exception.DictionaryException;
import exception.KeyNotFoundException;
import exception.ValidationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import pojo.KeyValuePair;
import pojo.KeyValuePairGroup;
import repository.DictionaryRepository;
import validation.Validation;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.IOException;
import java.util.List;


public abstract class BaseFileDictionaryService implements DictionaryService {
    protected final DictionaryRepository dictionaryRepository;
    protected final Validation validation;

    public BaseFileDictionaryService(DictionaryRepository DictionaryRepository, Validation validation) {
        this.dictionaryRepository = DictionaryRepository;
        this.validation = validation;
    }

    @Override
    public List<KeyValuePair> findAll() throws DictionaryException {
        try {
            return dictionaryRepository.findAll();
        } catch (IOException e) {
            throw new DictionaryException(e.getMessage());
        }
    }

    @Override
    public String removeEntryByKey(String key) throws DictionaryException {
        try {
            if (dictionaryRepository.removeEntryByKey(key)) {
                return "Успешно";
            } else {
                throw new KeyNotFoundException();
            }
        } catch (IOException | KeyNotFoundException e) {
            throw new DictionaryException(e.getMessage());
        }
    }

    @Override
    public KeyValuePair searchEntryByKey(String key) throws DictionaryException {
        try {
            return dictionaryRepository.searchEntryByKey(key).orElseThrow(KeyNotFoundException::new);
        } catch (IOException | KeyNotFoundException e) {
            throw new DictionaryException(e.getMessage());
        }
    }

    @Override
    public KeyValuePair addEntry(String key, String value) throws DictionaryException {
        try {
            if (validation.validate(key)) {
                KeyValuePair keyValuePair = new KeyValuePair(key, value);
                return dictionaryRepository.addEntry(keyValuePair);
            } else {
                throw new ValidationException(validation.getRequirements());
            }
        } catch (IOException | RuntimeException e) {
            throw new DictionaryException(e.getMessage());
        }
    }

    @Override
    public KeyValuePairGroup getPage(int page, int size) throws DictionaryException {
        try {
            return dictionaryRepository.getPage(page, size);
        } catch (IOException e) {
            throw new DictionaryException(e.getMessage());
        }
    }

    @Override
    public Document getDictionaryAsXML() throws DictionaryException {
        try {
            List<KeyValuePair> entries = findAll();
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element root = document.createElement("dictionary");
            document.appendChild(root);

            for (KeyValuePair entry : entries) {
                Element wordElement = document.createElement("word");
                root.appendChild(wordElement);

                Element englishElement = document.createElement("key");
                englishElement.appendChild(document.createTextNode(entry.getKey()));
                wordElement.appendChild(englishElement);

                Element russianElement = document.createElement("value");
                russianElement.appendChild(document.createTextNode(entry.getValue()));
                wordElement.appendChild(russianElement);
            }
            return document;
        } catch (Exception e) {
            throw new DictionaryException(e.getMessage());
        }
    }
}
