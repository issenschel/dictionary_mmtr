package com.example.dictionary_mmtr.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class KeyValuePairRequestDto {

    @NotBlank(message = "Ключ не может быть пустым")
    private String key;

    @NotBlank(message = "Значение не может быть пустым")
    private String value;
}
