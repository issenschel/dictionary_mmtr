package com.example.dictionary_mmtr.service;

import com.example.dictionary_mmtr.entity.ValidationType;
import com.example.dictionary_mmtr.repository.ValidationTypeRepository;
import com.example.dictionary_mmtr.enums.ValidationKind;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ValidationTypeService {
    private final ValidationTypeRepository validationTypeRepository;

    Optional<ValidationType> findByValidationType(ValidationKind validationKind){
       return validationTypeRepository.findByValidationKind(validationKind);
    }
}
