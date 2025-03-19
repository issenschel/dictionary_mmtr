package com.example.dictionary_mmtr.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CallbackNotificationDto {
    private String callbackUrl;
    private String accessToken;
    private String eventType;
    @JsonFormat(pattern = "yyyy-MM-dd''HH:mm:ss")
    private LocalDateTime operationTimestamp;
    private String key;
    private String value;
}