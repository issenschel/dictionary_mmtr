package com.example.dictionary_mmtr.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "dictionary_entry",
        uniqueConstraints = @UniqueConstraint(columnNames = {"key", "dictionary_type_id"}))
public class DictionaryEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne()
    @JoinColumn(name = "dictionary_type_id")
    private DictionaryType dictionaryType;

    @Column(name = "key")
    private String key;

    @Column(name = "processed_key")
    private String processedKey;

    @OneToMany(mappedBy = "dictionaryEntry", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DictionaryValue> values;
}
