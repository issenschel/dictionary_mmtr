package com.example.dictionary_mmtr.service;

import com.example.dictionary_mmtr.dto.DictionaryTypeDto;
import com.example.dictionary_mmtr.dto.ResponseDto;
import com.example.dictionary_mmtr.entity.DictionaryType;
import com.example.dictionary_mmtr.exception.DictionaryNotFoundException;
import com.example.dictionary_mmtr.repository.DictionaryTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DictionaryTypeService {
    private final DictionaryTypeRepository dictionaryTypeRepository;

    public DictionaryType createDictionaryType(DictionaryTypeDto dictionaryTypeDto) {
        DictionaryType dictionaryType = new DictionaryType();
        dictionaryType.setName(dictionaryTypeDto.getTableName());
        dictionaryType.setRegexPattern(dictionaryTypeDto.getRegexPattern());
        dictionaryType.setFilterSQL(dictionaryTypeDto.getValidateSQL());
        return dictionaryTypeRepository.save(dictionaryType);
    }

    public ResponseDto deleteDictionaryType(String dictionaryTypeName) {
        DictionaryType dictionaryType = dictionaryTypeRepository.findByName(dictionaryTypeName).orElseThrow(DictionaryNotFoundException::new);
        dictionaryType.setDeleted(true);
        dictionaryTypeRepository.save(dictionaryType);
        return new ResponseDto("deleted.dictionary.type");
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
