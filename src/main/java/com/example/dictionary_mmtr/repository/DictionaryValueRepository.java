package com.example.dictionary_mmtr.repository;

import com.example.dictionary_mmtr.entity.DictionaryEntry;
import com.example.dictionary_mmtr.entity.DictionaryValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DictionaryValueRepository extends JpaRepository<DictionaryValue, Integer> {

    Optional<DictionaryValue> findByValueAndDictionaryEntry(String key, DictionaryEntry dictionaryEntry);

    Optional<DictionaryValue> findByValue(String Value);
}
