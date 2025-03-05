package com.example.dictionary_mmtr.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "validation_type_id", nullable = false)
    @JsonIgnore
    private ValidationType validationType;

    @OneToMany(mappedBy = "dictionaryType", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<DictionaryEntry> entries;

}
