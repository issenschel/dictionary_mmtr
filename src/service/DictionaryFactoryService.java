package service;

import enums.DictionaryType;
import repository.BaseFileDictionaryRepository;
import utils.ConfigLoader;
import validation.BackspaceValidation;
import validation.LatinValidation;
import validation.NumberValidation;
import validation.Validation;

import java.nio.file.Paths;

public class DictionaryFactoryService {
    private final ConfigLoader configLoader;

    public DictionaryFactoryService() {
        this.configLoader = new ConfigLoader();
    }

    public DictionaryService createService(DictionaryType type) {
        Validation validation;

        switch (type) {
            case LATIN:
                validation = new LatinValidation();
                return new DuplicateDictionaryServiceBase(
                        new BaseFileDictionaryRepository(Paths.get(configLoader.getDictionaryValue(DictionaryType.LATIN)),
                                validation.getKeyTransformer()), validation);
            case NUMBER:
                validation = new NumberValidation();
                return new DuplicateDictionaryServiceBase(
                        new BaseFileDictionaryRepository(Paths.get(configLoader.getDictionaryValue(DictionaryType.NUMBER)),
                                validation.getKeyTransformer()), validation);
            case BACKSPACE:
                validation = new BackspaceValidation();
                return new UniqueDictionaryService(
                        new BaseFileDictionaryRepository(Paths.get(configLoader.getDictionaryValue(DictionaryType.BACKSPACE)),
                                validation.getKeyTransformer()), validation);
            default:
                throw new IllegalArgumentException("Неподдерживаемый тип словаря: " + type);
        }
    }

}
