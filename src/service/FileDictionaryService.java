package service;

import exception.ValidationException;
import repository.DictionaryRepository;
import validation.Validation;


public class FileDictionaryService implements DictionaryService{
    private final DictionaryRepository dictionaryRepository;
    private final Validation validation;

    public FileDictionaryService(DictionaryRepository DictionaryRepository, Validation validation) {
        this.dictionaryRepository = DictionaryRepository;
        this.validation = validation;
    }

    public void displayDictionary() {
        try {
            dictionaryRepository.displayDictionary();
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

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

    public void searchEntryByKey(String key) {
        try {
            System.out.println(dictionaryRepository.searchEntryByKey(key));
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

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
}
