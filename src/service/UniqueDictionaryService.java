package service;

import exception.KeyFoundException;
import exception.ValidationException;
import pojo.KeyValuePair;
import repository.DictionaryRepository;
import validation.Validation;

import java.io.IOException;

public class UniqueDictionaryService extends BaseFileDictionaryService {

    public UniqueDictionaryService(DictionaryRepository DictionaryRepository, Validation validation) {
        super(DictionaryRepository, validation);
    }

    @Override
    public KeyValuePair addEntry(String key, String value) {
        try {
            if (validation.validate(key)) {
                KeyValuePair keyValuePair = new KeyValuePair(key, value);
                dictionaryRepository.searchEntryByKey(key).ifPresent(t -> {
                    throw new KeyFoundException();
                });
                return dictionaryRepository.addEntry(keyValuePair);
            } else {
                throw new ValidationException(validation.getRequirements());
            }
        } catch (IOException | RuntimeException e) {
            System.out.println(e.getMessage());
            return new KeyValuePair();
        }
    }
}
