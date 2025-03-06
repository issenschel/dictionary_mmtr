package com.example.dictionary_mmtr.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class SubscriptionRequestDto {

    @NotBlank(message = "validation.dictionaryTypeId.notBlank")
    @Size(min = 1, message = "validation.dictionaryTypeId.size")
    private Integer dictionaryId;

    @NotBlank(message = "validation.callbackUrl.notBlank")
    private String callbackUrl;

    @NotBlank(message = "validation.accessToken.notBlank")
    private String accessToken;
}
