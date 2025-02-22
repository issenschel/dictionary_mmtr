package com.example.dictionary_mmtr.service;

import com.example.dictionary_mmtr.entity.DictionaryType;
import com.example.dictionary_mmtr.exception.DictionaryNotFoundException;
import com.example.dictionary_mmtr.exception.ValidationException;
import com.example.dictionary_mmtr.validation.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DictionaryValidationService {
    private final DictionaryTypeService dictionaryTypeService;

    public DictionaryType validateDictionaryType(String tableName) {
        DictionaryType dictionaryType = dictionaryTypeService.findDictionaryTypeByName(tableName)
                .orElseThrow(DictionaryNotFoundException::new);

        if (dictionaryType.getDeleted()) {
            throw new DictionaryNotFoundException();
        }

        return dictionaryType;
    }

    public void validateKey(DictionaryType dictionaryType, String key) {
        Validation validation = dictionaryType.getValidationType().getType().getValidation();
        if (!validation.validate(key)) {
            throw new ValidationException(validation.getRequirements());
        }
    }

    public String processKey(String key, DictionaryType dictionaryType) {
        Validation validation = dictionaryType.getValidationType().getType().getValidation();
        return validation.getKeyTransformer().apply(key);
    }
}
