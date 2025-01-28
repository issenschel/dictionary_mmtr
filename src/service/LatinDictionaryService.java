package service;

import repository.DictionaryRepository;
import validation.Validation;

public class LatinDictionaryService extends FileDictionaryService{
    public LatinDictionaryService(DictionaryRepository DictionaryRepository, Validation validation) {
        super(DictionaryRepository, validation);
    }
}
