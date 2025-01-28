package service;

import pojo.KeyValuePair;
import exception.KeyNotFoundException;
import exception.ValidationException;
import pojo.KeyValuePairGroup;
import repository.DictionaryRepository;
import validation.Validation;

import java.util.List;


public abstract class FileDictionaryService implements DictionaryService{
    protected final DictionaryRepository dictionaryRepository;
    protected final Validation validation;

    public FileDictionaryService(DictionaryRepository DictionaryRepository, Validation validation) {
        this.dictionaryRepository = DictionaryRepository;
        this.validation = validation;
    }

    @Override
    public List<KeyValuePair> findAll() {
        try {
            return dictionaryRepository.findAll();
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public void removeEntryByKey(String key) {
        try {
            if(dictionaryRepository.removeEntryByKey(key)){
                System.out.println("Успешно");
            }else{
                System.out.println("Ключ не найден");
            }
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void searchEntryByKey(String key) {
        try {
            System.out.println(dictionaryRepository.searchEntryByKey(key).orElseThrow(KeyNotFoundException::new));
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void addEntry(String key, String value) {
        try {
            if (validation.validate(key)) {
                dictionaryRepository.addEntry(key, value);
                System.out.println("Успешно");
            }else {
                throw new ValidationException(validation.getRequirements());
            }
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public KeyValuePairGroup pagination(int page, int size) {
        return dictionaryRepository.pagination(page, size);
    }
}
