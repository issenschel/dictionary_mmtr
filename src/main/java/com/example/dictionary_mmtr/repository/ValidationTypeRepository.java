package com.example.dictionary_mmtr.repository;

import com.example.dictionary_mmtr.entity.ValidationType;
import com.example.dictionary_mmtr.enums.ValidationKind;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ValidationTypeRepository extends JpaRepository<ValidationType, Long> {

    Optional<ValidationType> findByValidationKind(ValidationKind validationKind);
}
