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
                return new LatinDictionaryService(
                        new LatinDictionaryRepository(Paths.get(configLoader.getDictionaryValue(DictionaryType.LATIN))),
                        new LatinValidation()
                );
            case NUMBER:
                return new NumberDictionaryService(
                        new NumberDictionaryRepository(Paths.get(configLoader.getDictionaryValue(DictionaryType.NUMBER))),
                        new NumberValidation()
                );
            case BACKSPACE:
                return new BackspaceDictionaryService(
                        new BackspaceDictionaryRepository(Paths.get(configLoader.getDictionaryValue(DictionaryType.BACKSPACE))),
                        new BackspaceValidation()
                );
            default:
                throw new IllegalArgumentException("Неподдерживаемый тип словаря: " + type);
        }
    }

}
