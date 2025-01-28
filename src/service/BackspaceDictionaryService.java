package service;

import exception.AddEntryException;
import exception.ValidationException;
import repository.DictionaryRepository;
import validation.Validation;

public class BackspaceDictionaryService extends FileDictionaryService {

    public BackspaceDictionaryService(DictionaryRepository DictionaryRepository, Validation validation) {
        super(DictionaryRepository, validation);
    }

    @Override
    public void addEntry(String key, String value) {
        try {
            if (validation.validate(key)) {
                dictionaryRepository.searchEntryByKey(key).ifPresent(t -> {throw new AddEntryException();});
                dictionaryRepository.addEntry(key, value);
            }else {
                throw new ValidationException(validation.getRequirements());
            }
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }
}
