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
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

public abstract class BaseFileDictionaryService implements DictionaryService {
    protected final DictionaryRepository dictionaryRepository;
    protected final Validation validation;

    public BaseFileDictionaryService(DictionaryRepository DictionaryRepository, Validation validation) {
        this.dictionaryRepository = DictionaryRepository;
        this.validation = validation;
    }

    public List<KeyValuePair> findAll() throws DictionaryException {
        try {
            return dictionaryRepository.findAll();
        } catch (IOException e) {
            throw new DictionaryException(e.getMessage());
        }
    }

    public String removeEntryByKey(String key) throws DictionaryException {
        try {
            validate(key);
            if (dictionaryRepository.removeEntryByKey(key)) {
                return "Успешно";
            } else {
                throw new KeyNotFoundException();
            }
        } catch (IOException | KeyNotFoundException e) {
            throw new DictionaryException(e.getMessage());
        }
    }

    public KeyValuePair searchEntryByKey(String key) throws DictionaryException {
        try {
            validate(key);
            return dictionaryRepository.searchEntryByKey(key).orElseThrow(KeyNotFoundException::new);
        } catch (IOException | KeyNotFoundException e) {
            throw new DictionaryException(e.getMessage());
        }
    }

    public KeyValuePair addEntry(String key, String value) throws DictionaryException {
        try {
            validate(key);
            KeyValuePair keyValuePair = new KeyValuePair(key, value);
            return dictionaryRepository.addEntry(keyValuePair);
        } catch (IOException | RuntimeException e) {
            throw new DictionaryException(e.getMessage());
        }
    }

    public KeyValuePairGroup getPage(int page, int size) throws DictionaryException {
        try {
            return dictionaryRepository.getPage(page, size);
        } catch (IOException e) {
            throw new DictionaryException(e.getMessage());
        }
    }

    public String getDictionaryAsXML() throws DictionaryException {
        try(StringWriter stringWriter = new StringWriter()) {
            List<KeyValuePair> entries = findAll();
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element root = document.createElement("dictionary");
            document.appendChild(root);

            for (KeyValuePair entry : entries) {
                Element wordElement = document.createElement("entry");
                root.appendChild(wordElement);

                Element englishElement = document.createElement("key");
                englishElement.appendChild(document.createTextNode(entry.getKey()));
                wordElement.appendChild(englishElement);

                Element russianElement = document.createElement("value");
                russianElement.appendChild(document.createTextNode(entry.getValue()));
                wordElement.appendChild(russianElement);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(stringWriter);
            transformer.transform(source, result);

            return stringWriter.toString();
        } catch (Exception e) {
            throw new DictionaryException(e.getMessage());
        }
    }

    private void validate(String key){
        if(!validation.validate(key)){
            throw new ValidationException(validation.getRequirements());
        }
    }
}
