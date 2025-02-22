package com.example.dictionary_mmtr.dto;

import com.example.dictionary_mmtr.enums.ValidationKind;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class DictionaryTypeRequest {

    @NotBlank(message = "validation.dictionaryTypeName.notBlank")
    @Size(min = 2, message = "validation.dictionaryTypeName.size")
    private String dictionaryTypeName;

    @NotNull(message = "validation.validationKind.notNull")
    private ValidationKind validationKind;
}
