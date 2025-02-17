package com.example.dictionary_mmtr.repository;
import com.example.dictionary_mmtr.entity.DictionaryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DictionaryTypeRepository extends JpaRepository<DictionaryType, Long> {
    Optional<DictionaryType> findByName(String name);
}
