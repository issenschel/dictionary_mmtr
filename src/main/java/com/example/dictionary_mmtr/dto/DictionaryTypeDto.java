package com.example.dictionary_mmtr.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DictionaryTypeDto {

    @NotBlank(message = "Имя таблицы не может быть пустым")
    private String tableName;

    @NotBlank(message = "Регулярное выражение для ключа не может быть пустым")
    private String regexPattern;

    @NotBlank(message = "SQL запрос для фильтрации не может быть пустым")
    private String validateSQL;
}
