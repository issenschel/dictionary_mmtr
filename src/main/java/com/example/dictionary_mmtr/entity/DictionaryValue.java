package com.example.dictionary_mmtr.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "dictionary_value",
        uniqueConstraints = @UniqueConstraint(columnNames = {"dictionary_entry_id", "value"}))
public class DictionaryValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "dictionary_entry_id")
    private DictionaryEntry dictionaryEntry;

    @Column(name = "value")
    private String value;
}