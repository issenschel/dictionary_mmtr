package com.example.dictionary_mmtr.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KeyValuesDto {
    private String key;
    private List<String> values;
    private String value;

    public KeyValuesDto(String key, List<String> values) {
        this.key = key;
        this.values = values;
        this.value = null;
    }

    public KeyValuesDto(String key, String value) {
        this.key = key;
        this.value = value;
        this.values = null;
    }
}
