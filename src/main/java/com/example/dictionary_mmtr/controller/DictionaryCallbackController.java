package com.example.dictionary_mmtr.controller;

import com.example.dictionary_mmtr.dto.SubscriptionRequestDto;
import com.example.dictionary_mmtr.entity.DictionaryCallbackSubscription;
import com.example.dictionary_mmtr.service.DictionaryCallbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/dictionaries/callbacks/subscriptions")
@RequiredArgsConstructor
public class DictionaryCallbackController {

    private final DictionaryCallbackService callbackService;

    @PostMapping
    public DictionaryCallbackSubscription subscribeToDictionary(@RequestBody @Valid SubscriptionRequestDto request) {
        return callbackService.subscribeToDictionary(request.getDictionaryId(), request.getCallbackUrl(), request.getAccessToken());
    }

    @DeleteMapping
    public ResponseEntity<?> unsubscribeFromDictionary(@RequestParam UUID subscriptionId) {
        return ResponseEntity.ok().body(callbackService.unsubscribeFromDictionary(subscriptionId));
    }

}