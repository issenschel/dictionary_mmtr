package com.example.dictionary_mmtr.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CallbackNotificationDto {
    private String key;
    private String value;
    private LocalDateTime operationTimestamp;
}
