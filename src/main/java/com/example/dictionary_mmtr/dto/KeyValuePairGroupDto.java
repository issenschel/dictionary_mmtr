package com.example.dictionary_mmtr.dto;

import com.example.dictionary_mmtr.entity.BaseDictionary;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class KeyValuePairGroupDto {
    private List<? extends BaseDictionary> dictionary;
    private Integer count;

    public KeyValuePairGroupDto() {
    }
}

