package com.example.dictionary_mmtr.repository;

import com.example.dictionary_mmtr.enums.DictionaryType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DictionaryTypeRepository extends JpaRepository<DictionaryType, Long> {
}
