package com.example.dictionary_mmtr.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "dictionary_type")
public class DictionaryType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "deleted")
    private Boolean deleted = false;

}
