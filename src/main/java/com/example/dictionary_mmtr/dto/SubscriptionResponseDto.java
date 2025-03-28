package com.example.dictionary_mmtr.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
public class SubscriptionResponseDto {

    @NotBlank(message = "validation.subscriptionId.notBlank")
    private final UUID subscriptionId;
}
