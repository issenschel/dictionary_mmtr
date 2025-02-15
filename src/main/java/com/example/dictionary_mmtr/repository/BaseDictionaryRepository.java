package com.example.dictionary_mmtr.repository;

import com.example.dictionary_mmtr.entity.BaseDictionary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;
import java.util.stream.Stream;

@NoRepositoryBean
public interface BaseDictionaryRepository<T extends BaseDictionary> extends JpaRepository<T, Integer> {

    Optional<T> findByKey(String key);

    void deleteByKey(String key);

    @Query("SELECT e FROM #{#entityName} e")
    Stream<T> streamAllEntities();
}
