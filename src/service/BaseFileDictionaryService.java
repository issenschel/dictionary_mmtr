package service;

import exception.KeyNotFoundException;
import exception.ValidationException;
import pojo.KeyValuePair;
import pojo.KeyValuePairGroup;
import repository.DictionaryRepository;
import validation.Validation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public abstract class BaseFileDictionaryService implements DictionaryService {
    protected final DictionaryRepository dictionaryRepository;
    protected final Validation validation;

    public BaseFileDictionaryService(DictionaryRepository DictionaryRepository, Validation validation) {
        this.dictionaryRepository = DictionaryRepository;
        this.validation = validation;
    }

    @Override
    public List<KeyValuePair> findAll() {
        try {
            return dictionaryRepository.findAll();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public String removeEntryByKey(String key) {
        try {
            if (dictionaryRepository.removeEntryByKey(key)) {
                return "Успешно";
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return "Ключ не найден";
    }

    @Override
    public KeyValuePair searchEntryByKey(String key) {
        try {
            return dictionaryRepository.searchEntryByKey(key).orElseThrow(KeyNotFoundException::new);
        } catch (IOException | RuntimeException e) {
            System.out.println(e.getMessage());
            return new KeyValuePair();
        }
    }

    @Override
    public KeyValuePair addEntry(String key, String value) {
        try {
            if (validation.validate(key)) {
                KeyValuePair keyValuePair = new KeyValuePair(key, value);
                return dictionaryRepository.addEntry(keyValuePair);
            } else {
                throw new ValidationException(validation.getRequirements());
            }
        } catch (IOException | RuntimeException e) {
            return new KeyValuePair();
        }
    }

    @Override
    public KeyValuePairGroup pagination(int page, int size) {
        try {
            return dictionaryRepository.pagination(page, size);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return new KeyValuePairGroup();
    }
}
