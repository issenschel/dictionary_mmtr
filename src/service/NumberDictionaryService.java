package service;

import repository.DictionaryRepository;
import validation.Validation;

public class NumberDictionaryService extends FileDictionaryService{
    public NumberDictionaryService(DictionaryRepository DictionaryRepository, Validation validation) {
        super(DictionaryRepository, validation);
    }
}
