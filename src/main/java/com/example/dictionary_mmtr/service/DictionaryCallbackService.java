package com.example.dictionary_mmtr.service;

import com.example.dictionary_mmtr.dto.CallbackNotificationDto;
import com.example.dictionary_mmtr.dto.ResponseDto;
import com.example.dictionary_mmtr.entity.DictionaryCallbackSubscription;
import com.example.dictionary_mmtr.entity.DictionaryType;
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

    //Временная заглушка
    public void notifySubscribers(DictionaryType dictionaryType, String eventType, Object object) {
        CallbackNotificationDto callbackNotificationDto = new CallbackNotificationDto();
//        callbackNotificationDto.setKey(keyValuePairDto.getKey());
//        callbackNotificationDto.setValue(keyValuePairDto.getValue());
        callbackNotificationDto.setOperationTimestamp(LocalDateTime.now());
        callbackNotificationDto.setEventType(eventType);

        subscriptionRepository.findByDictionaryType(dictionaryType).forEach(subscription -> {
            callbackNotificationDto.setCallbackUrl(subscription.getCallbackUrl());
            callbackNotificationDto.setAccessToken(subscription.getAccessToken());
            rabbitTemplate.convertAndSend(rabbitQueueName, callbackNotificationDto);
        });
    }
}