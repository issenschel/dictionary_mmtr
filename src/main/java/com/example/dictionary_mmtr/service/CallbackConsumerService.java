package com.example.dictionary_mmtr.service;

import com.example.dictionary_mmtr.dto.CallbackNotificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CallbackConsumerService {

    private final RestTemplate restTemplate;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbit.queue.name}")
    private String rabbitQueueName;

    @RabbitListener(queues = "#{@rabbitQueueName}")
    @Async
    public void receiveMessage(CallbackNotificationDto callbackNotificationDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("access-token", callbackNotificationDto.getAccessToken());
        headers.set("event-type", callbackNotificationDto.getEventType());

        HttpEntity<CallbackNotificationDto> requestEntity = new HttpEntity<>(callbackNotificationDto, headers);

        CompletableFuture.supplyAsync(() -> restTemplate.exchange(callbackNotificationDto.getCallbackUrl(), HttpMethod.POST, requestEntity, Void.class))
                .orTimeout(30, TimeUnit.SECONDS)
                .exceptionally(ex -> {
                    rabbitTemplate.convertAndSend(rabbitQueueName, callbackNotificationDto);
                    return null;
                });
    }

    @Bean
    public String rabbitQueueName() {
        return rabbitQueueName;
    }
}