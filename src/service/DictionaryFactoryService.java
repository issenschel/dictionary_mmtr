package service;

import enums.DictionaryType;
import repository.*;
import utils.ConfigLoader;
import validation.BackspaceValidation;
import validation.LatinValidation;
import validation.NumberValidation;

import java.nio.file.Paths;

public class DictionaryFactoryService {
    private final ConfigLoader configLoader;

    public DictionaryFactoryService() {
        this.configLoader = new ConfigLoader();
    }

    public DictionaryService createService(DictionaryType type) {
        switch (type) {
            case LATIN:
                return new LatinDictionaryServiceBase(
                        new LatinDictionaryRepositoryRepository(Paths.get(configLoader.getDictionaryValue(DictionaryType.LATIN))),
                        new LatinValidation()
                );
            case NUMBER:
                return new NumberDictionaryServiceBase(
                        new NumberDictionaryRepositoryRepository(Paths.get(configLoader.getDictionaryValue(DictionaryType.NUMBER))),
                        new NumberValidation()
                );
            case BACKSPACE:
                return new BackspaceDictionaryServiceBase(
                        new BackspaceDictionaryRepositoryRepository(Paths.get(configLoader.getDictionaryValue(DictionaryType.BACKSPACE))),
                        new BackspaceValidation()
                );
            default:
                throw new IllegalArgumentException("Неподдерживаемый тип словаря: " + type);
        }
    }

}
