package com.example.dictionary_mmtr.entity;

import lombok.Data;

import javax.persistence.*;

@MappedSuperclass
@Data
public abstract class BaseDictionary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "key")
    private String key;

    @Column(name = "value")
    private String value;

    @Column(name = "deleted")
    private Boolean deleted = false;
}
