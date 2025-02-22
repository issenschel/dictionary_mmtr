package com.example.dictionary_mmtr.repository;

import com.example.dictionary_mmtr.entity.DictionaryEntry;
import com.example.dictionary_mmtr.entity.DictionaryType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface DictionaryEntryRepository extends JpaRepository<DictionaryEntry, Integer> {

    @EntityGraph(attributePaths = "values")
    Optional<DictionaryEntry> findByProcessedKeyAndDictionaryType(String processedKey, DictionaryType dictionaryType);

    @EntityGraph(attributePaths = "values")
    Page<DictionaryEntry> findByDictionaryType(DictionaryType dictionaryType, Pageable pageable);

    @EntityGraph(attributePaths = "values")
    Stream<DictionaryEntry> streamByDictionaryType(DictionaryType dictionaryType);

    @Query("SELECT de FROM DictionaryEntry de WHERE de.dictionaryType = :dictionaryType " +
           "AND (:keyFilter IS NULL OR de.key = :keyFilter) " +
           "AND (:valueFilter IS NULL OR EXISTS (SELECT 1 FROM de.values dv WHERE dv.value LIKE %:valueFilter%))")
    @EntityGraph(attributePaths = "values")
    Page<DictionaryEntry> findByDictionaryTypeAndFilters(@Param("dictionaryType") DictionaryType dictionaryType,
                                                         Pageable pageable,
                                                         @Param("keyFilter") String keyFilter,
                                                         @Param("valueFilter") String valueFilter);

    @Query("SELECT de FROM DictionaryEntry de " +
           "WHERE de.dictionaryType IN :dictionaryTypes " +
           "AND (:keyFilter IS NULL OR de.key = :keyFilter) " +
           "AND (:valueFilter IS NULL OR EXISTS (SELECT 1 FROM de.values dv WHERE dv.value LIKE %:valueFilter%))")
    @EntityGraph(attributePaths = {"dictionaryType", "dictionaryType.validationType", "values"})
    Page<DictionaryEntry> findAllDictionaryEntriesAcrossAllDictionaries(@Param("dictionaryTypes") List<DictionaryType> dictionaryTypes,
                                                                        Pageable pageable,
                                                                        @Param("keyFilter") String keyFilter,
                                                                        @Param("valueFilter") String valueFilter);

}
