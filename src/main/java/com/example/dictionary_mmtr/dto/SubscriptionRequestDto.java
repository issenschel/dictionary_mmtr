package com.example.dictionary_mmtr.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SubscriptionRequestDto {

    @NotNull(message = "validation.dictionaryTypeId.notBlank")
    @Min(value = 1, message = "validation.dictionaryTypeId.size")
    private Integer dictionaryId;

    @NotBlank(message = "validation.callbackUrl.notBlank")
    private String callbackUrl;

    @NotBlank(message = "validation.accessToken.notBlank")
    private String accessToken;
}
