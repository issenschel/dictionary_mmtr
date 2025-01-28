package service;

import repository.DictionaryRepository;
import validation.Validation;

public class LatinDictionaryServiceBase extends BaseFileDictionaryService {
    public LatinDictionaryServiceBase(DictionaryRepository DictionaryRepository, Validation validation) {
        super(DictionaryRepository, validation);
    }
}
