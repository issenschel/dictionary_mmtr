package service;

import repository.DictionaryRepository;
import validation.Validation;

public class DuplicateDictionaryServiceBase extends BaseFileDictionaryService {
    public DuplicateDictionaryServiceBase(DictionaryRepository DictionaryRepository, Validation validation) {
        super(DictionaryRepository, validation);
    }
}
