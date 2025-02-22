package com.example.dictionary_mmtr.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class KeyValuePairRequestDto {

    @NotBlank(message = "validation.key.notBlank")
    private String key;

    @NotBlank(message = "validation.value.notBlank")
    private String value;
}
