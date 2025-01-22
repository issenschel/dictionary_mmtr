package service;

import dto.DictionaryDto;
import exception.ValidationException;
import repository.DictionaryRepository;
import validation.Validation;

import java.util.List;


public class FileDictionaryService implements DictionaryService{
    private final DictionaryRepository dictionaryRepository;
    private final Validation validation;

    public FileDictionaryService(DictionaryRepository DictionaryRepository, Validation validation) {
        this.dictionaryRepository = DictionaryRepository;
        this.validation = validation;
    }

    public List<DictionaryDto> findAll() {
        try {
            return dictionaryRepository.findAll();
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return null;
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
