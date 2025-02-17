package com.example.dictionary_mmtr.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    @Column(name = "deleted")
    private Boolean deleted = false;

    @JsonIgnore
    @Column(name = "regex_pattern")
    private String regexPattern;

    @JsonIgnore
    @Column(name = "filter_sql")
    private String filterSQL;

}
