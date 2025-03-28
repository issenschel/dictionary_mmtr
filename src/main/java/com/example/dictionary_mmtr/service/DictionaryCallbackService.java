package com.example.dictionary_mmtr.service;

import com.example.dictionary_mmtr.dto.CallbackNotificationDto;
import com.example.dictionary_mmtr.dto.ResponseDto;
import com.example.dictionary_mmtr.entity.DictionaryCallbackSubscription;
import com.example.dictionary_mmtr.entity.DictionaryEntry;
import com.example.dictionary_mmtr.entity.DictionaryType;
import com.example.dictionary_mmtr.entity.DictionaryValue;
import com.example.dictionary_mmtr.exception.DictionaryNotFoundException;
import com.example.dictionary_mmtr.repository.DictionaryCallbackSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class DictionaryCallbackService {
    private final DictionaryTypeService dictionaryTypeService;
    private final DictionaryCallbackSubscriptionRepository subscriptionRepository;
    private final RabbitTemplate rabbitTemplate;
    private final MessageSource messageSource;

    @Value("${rabbit.queue.name}")
    private String rabbitQueueName;

    @Transactional
    public DictionaryCallbackSubscription subscribeToDictionary(int dictionaryTypeId, String callbackUrl, String accessToken) {
        DictionaryType dictionaryType = dictionaryTypeService.findDictionaryTypeById(dictionaryTypeId).orElseThrow(DictionaryNotFoundException::new);
        return subscriptionRepository.findByDictionaryTypeAndCallbackUrl(dictionaryType, callbackUrl)
                .orElseGet(() -> createNewSubscription(dictionaryType, callbackUrl, accessToken));
    }

    private DictionaryCallbackSubscription createNewSubscription(DictionaryType dictionaryType, String callbackUrl, String accessToken) {
        DictionaryCallbackSubscription subscription = new DictionaryCallbackSubscription();
        subscription.setDictionaryType(dictionaryType);
        subscription.setCallbackUrl(callbackUrl);
        subscription.setAccessToken(accessToken);
        return subscriptionRepository.save(subscription);
    }

    @Transactional
    public ResponseDto unsubscribeFromDictionary(UUID subscriptionId) {
        subscriptionRepository.deleteBySubscriptionId(subscriptionId);
        return new ResponseDto(messageSource.getMessage("success.unsubscribe", null, LocaleContextHolder.getLocale()));
    }

    public void notifySubscribers(DictionaryType dictionaryType, String eventType, DictionaryEntry dictionaryEntry) {

        List<DictionaryCallbackSubscription> dictionaryCallbackSubscriptions = subscriptionRepository.findByDictionaryType(dictionaryType);

        for (DictionaryValue value : dictionaryEntry.getValues()) {
            CallbackNotificationDto callbackNotificationDto = new CallbackNotificationDto();
            callbackNotificationDto.setKey(dictionaryEntry.getKey());
            callbackNotificationDto.setOperationTimestamp(LocalDateTime.now());
            callbackNotificationDto.setEventType(eventType);
            callbackNotificationDto.setValue(value.getValue());

            dictionaryCallbackSubscriptions.forEach(subscription -> {
                callbackNotificationDto.setCallbackUrl(subscription.getCallbackUrl());
                callbackNotificationDto.setAccessToken(subscription.getAccessToken());
                rabbitTemplate.convertAndSend(rabbitQueueName, callbackNotificationDto);
            });
        }
    }
}