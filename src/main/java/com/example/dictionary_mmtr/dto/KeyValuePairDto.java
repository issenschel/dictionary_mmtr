package com.example.dictionary_mmtr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KeyValuePairDto {
    private String key;
    private String value;

    public KeyValuePairDto() {
    }
}
