package com.example.dictionary_mmtr.dto;

import lombok.Data;

@Data
public class DictionaryQueryDto {
    private String dictionaryType = "latin";
    private int page = 1;
    private int size = 10;
    private String keyFilter;
    private String valueFilter;
    private boolean searchAll = false;
    private boolean useLegacyFormat = true;
}
