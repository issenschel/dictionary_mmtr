package service;

import repository.DictionaryRepository;
import validation.Validation;

public class NumberDictionaryServiceBase extends BaseFileDictionaryService {
    public NumberDictionaryServiceBase(DictionaryRepository DictionaryRepository, Validation validation) {
        super(DictionaryRepository, validation);
    }
}
