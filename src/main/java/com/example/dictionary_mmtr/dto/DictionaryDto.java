package com.example.dictionary_mmtr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class    DictionaryDto {
    private String key;
    private List<String> values;
}
