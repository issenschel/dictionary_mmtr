package com.example.dictionary_mmtr.repository;

import com.example.dictionary_mmtr.entity.DictionaryCallbackSubscription;
import com.example.dictionary_mmtr.entity.DictionaryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DictionaryCallbackSubscriptionRepository extends JpaRepository<DictionaryCallbackSubscription, Integer> {

    List<DictionaryCallbackSubscription> findByDictionaryType(DictionaryType dictionaryType);

    void deleteBySubscriptionId(UUID subscriptionId);

    Optional<DictionaryCallbackSubscription> findByDictionaryTypeAndCallbackUrl(DictionaryType dictionaryType, String callbackUrl);
}
