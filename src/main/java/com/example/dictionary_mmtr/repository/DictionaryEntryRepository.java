package com.example.dictionary_mmtr.repository;

import com.example.dictionary_mmtr.entity.DictionaryEntry;
import com.example.dictionary_mmtr.entity.DictionaryType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface DictionaryEntryRepository extends JpaRepository<DictionaryEntry, Integer> {

    @EntityGraph(attributePaths = "values")
    Optional<DictionaryEntry> findByKeyAndDictionaryType(String key, DictionaryType dictionaryType);

    @EntityGraph(attributePaths = "values")
    Page<DictionaryEntry> findByDictionaryType(DictionaryType dictionaryType, Pageable pageable);

    @EntityGraph(attributePaths = "values")
    Stream<DictionaryEntry> streamByDictionaryType(DictionaryType dictionaryType);

}
