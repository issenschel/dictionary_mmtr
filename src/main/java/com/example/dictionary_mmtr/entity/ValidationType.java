package com.example.dictionary_mmtr.entity;

import com.example.dictionary_mmtr.enums.ValidationKind;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "validation_type")
public class ValidationType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private ValidationKind validationKind;

    @OneToMany(mappedBy = "validationType", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<DictionaryType> dictionaryTypes;

    public ValidationKind getType() {
        return validationKind;
    }

}