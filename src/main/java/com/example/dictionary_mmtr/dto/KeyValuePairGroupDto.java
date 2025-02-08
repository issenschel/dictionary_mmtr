package com.example.dictionary_mmtr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class KeyValuePairGroupDto {
    private List<KeyValuePairDto> dictionary;
    private Integer count;

    public KeyValuePairGroupDto() {
    }
}

