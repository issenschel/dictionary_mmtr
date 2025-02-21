package com.example.dictionary_mmtr.service;

import com.example.dictionary_mmtr.dto.DictionaryTypeRequest;
import com.example.dictionary_mmtr.entity.DictionaryType;
import com.example.dictionary_mmtr.entity.ValidationType;
import com.example.dictionary_mmtr.exception.DictionaryNotFoundException;
import com.example.dictionary_mmtr.exception.KeyFoundException;
import com.example.dictionary_mmtr.exception.ValidationException;
import com.example.dictionary_mmtr.repository.DictionaryTypeRepository;
import com.example.dictionary_mmtr.enums.ValidationKind;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DictionaryTypeService {
    private final DictionaryTypeRepository dictionaryTypeRepository;
    private final ValidationTypeService validationTypeService;

    @Transactional
    public DictionaryType createDictionaryType(DictionaryTypeRequest dictionaryTypeRequest) {

        DictionaryType dictionaryType = new DictionaryType();
        dictionaryType.setName(dictionaryTypeRequest.getDictionaryTypeName());
        ValidationType validationType = validationTypeService.findByValidationType(dictionaryTypeRequest.getValidationKind())
                .orElseThrow(KeyFoundException::new);
        dictionaryType.setValidationType(validationType);

        return dictionaryTypeRepository.save(dictionaryType);
    }

    @Transactional
    public void deleteDictionaryType(String dictionaryTypeName) {
        DictionaryType dictionaryType = dictionaryTypeRepository.findByName(dictionaryTypeName).orElseThrow(DictionaryNotFoundException::new);
        dictionaryType.setDeleted(true);
        dictionaryTypeRepository.save(dictionaryType);
    }

    public Optional<DictionaryType> findDictionaryTypeByName(String dictionaryTypeName) {
        return dictionaryTypeRepository.findByName(dictionaryTypeName);
    }

    public List<DictionaryType> getActiveDictionaryTypes() {
        return dictionaryTypeRepository.findAll().stream()
                .filter(dictionaryType -> !dictionaryType.getDeleted())
                .collect(Collectors.toList());
    }
}
