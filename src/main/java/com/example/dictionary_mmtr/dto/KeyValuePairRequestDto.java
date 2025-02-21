package com.example.dictionary_mmtr.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class KeyValuePairRequestDto {

    @NotBlank(message = "Ключ не может быть пустым")
    private String key;

    @NotBlank(message = "Значение не может быть пустым")
    private String value;
}
