package com.example.dictionary_mmtr.entity;

import lombok.Data;

import java.util.List;

@Data
public class BaseDictionary {

    private Integer id;

    private String key;

    private List<String> values;
}
