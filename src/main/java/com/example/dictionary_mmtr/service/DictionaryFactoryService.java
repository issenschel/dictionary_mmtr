package com.example.dictionary_mmtr.service;

import com.example.dictionary_mmtr.enums.DictionaryType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DictionaryFactoryService {

    private final DictionaryService latinService;
    private final DictionaryService numberService;
    private final DictionaryService backspaceService;

    @Autowired
    public DictionaryFactoryService(DictionaryService latinService,
                                    DictionaryService numberService,
                                    DictionaryService backspaceService) {
        this.latinService = latinService;
        this.numberService = numberService;
        this.backspaceService = backspaceService;
    }

    public DictionaryService getService(DictionaryType dictionaryType) {
        switch (dictionaryType) {
            case LATIN:
                return latinService;
            case NUMBER:
                return numberService;
            case BACKSPACE:
                return backspaceService;
            default:
                throw new IllegalArgumentException("Некорректный тип словаря: " + dictionaryType);
        }
    }
}
