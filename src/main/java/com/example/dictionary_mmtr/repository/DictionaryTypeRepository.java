package com.example.dictionary_mmtr.repository;
import com.example.dictionary_mmtr.entity.DictionaryType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DictionaryTypeRepository extends JpaRepository<DictionaryType, Integer> {
    @EntityGraph(attributePaths = "validationType")
    Optional<DictionaryType> findByName(String name);

    @Query("SELECT dt FROM DictionaryType dt " +
           "LEFT JOIN DictionaryEntry de ON de.dictionaryType.id = dt.id " +
           "WHERE dt.deleted = false " +
           "GROUP BY dt.id " +
           "ORDER BY COALESCE(SUM(de.searchCount), 0) DESC")
    List<DictionaryType> findActiveDictionaryTypesOrderByPopularity();



}
