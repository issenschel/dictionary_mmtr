package com.example.dictionary_mmtr.service;

import com.example.dictionary_mmtr.dto.ResponseDto;
import com.example.dictionary_mmtr.entity.DictionaryCallbackSubscription;
import com.example.dictionary_mmtr.entity.DictionaryType;
import com.example.dictionary_mmtr.exception.DictionaryNotFoundException;
import com.example.dictionary_mmtr.repository.DictionaryCallbackSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DictionaryCallbackService {
    private final DictionaryTypeService dictionaryTypeService;
    private final DictionaryCallbackSubscriptionRepository subscriptionRepository;
    private final RestTemplate restTemplate;
    private final MessageSource messageSource;

    @Transactional
    public DictionaryCallbackSubscription subscribeToDictionary(int dictionaryTypeId, String callbackUrl, String accessToken) {
        DictionaryType dictionaryType = dictionaryTypeService.findDictionaryTypeById(dictionaryTypeId).orElseThrow(DictionaryNotFoundException::new);
        DictionaryCallbackSubscription subscription = new DictionaryCallbackSubscription();
        subscription.setDictionaryType(dictionaryType);
        subscription.setCallbackUrl(callbackUrl);
        subscription.setAccessToken(accessToken);
        return subscriptionRepository.save(subscription);
    }

    @Transactional
    public ResponseDto unsubscribeFromDictionary(UUID subscriptionUUID) {
        subscriptionRepository.deleteBySubscriptionId(subscriptionUUID);
        return new ResponseDto(messageSource.getMessage("success.unsubscribe", null, LocaleContextHolder.getLocale()));
    }

    public void notifySubscribers(DictionaryType dictionaryType, String eventType, Object payload) {
        subscriptionRepository.findByDictionaryType(dictionaryType).forEach(subscription -> {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("eventType", eventType);
            requestBody.put("payload", payload);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + subscription.getAccessToken());
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Void> response = restTemplate.exchange(
                    subscription.getCallbackUrl(),
                    HttpMethod.POST,
                    requestEntity,
                    Void.class
            );
        });
    }
}