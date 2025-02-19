package com.example.dictionary_mmtr.dto;

import com.example.dictionary_mmtr.entity.DictionaryEntry;
import com.example.dictionary_mmtr.entity.DictionaryType;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class KeyValuePairGroupDto {
    private List<DictionaryDto> dictionary;
    private Integer count;

    public KeyValuePairGroupDto() {
    }
}

