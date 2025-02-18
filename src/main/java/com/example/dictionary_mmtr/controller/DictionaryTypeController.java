package com.example.dictionary_mmtr.controller;

import com.example.dictionary_mmtr.annotation.AdminAccess;
import com.example.dictionary_mmtr.dto.DictionaryTypeDto;
import com.example.dictionary_mmtr.entity.DictionaryType;
import com.example.dictionary_mmtr.service.DictionaryTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dictionary-type")
@RequiredArgsConstructor
public class DictionaryTypeController {

    private final DictionaryTypeService dictionaryTypeService;

    @GetMapping()
    public List<DictionaryType> getAllActiveDictionaryTypes() {
        return dictionaryTypeService.getActiveDictionaryTypes();
    }

    @PostMapping()
    @AdminAccess
    public DictionaryType createDictionaryType(@RequestBody @Valid DictionaryTypeDto dictionaryTypeDto) {
        return dictionaryTypeService.createDictionaryType(dictionaryTypeDto);
    }

    @DeleteMapping("/{dictionaryTypeName}")
    @AdminAccess
    public void deleteDictionaryType(@PathVariable String dictionaryTypeName) {
        dictionaryTypeService.deleteDictionaryType(dictionaryTypeName);
    }
}
