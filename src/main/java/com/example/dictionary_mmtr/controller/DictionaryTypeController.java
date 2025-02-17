package com.example.dictionary_mmtr.controller;

import com.example.dictionary_mmtr.annotation.AdminAccess;

import com.example.dictionary_mmtr.dto.DictionaryTypeDto;
import com.example.dictionary_mmtr.dto.ResponseDto;
import com.example.dictionary_mmtr.entity.DictionaryType;
import com.example.dictionary_mmtr.service.BaseDictionaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dictionary-type")
@RequiredArgsConstructor
public class DictionaryTypeController {

    private final BaseDictionaryService dictionaryService;

    @GetMapping()
    public List<DictionaryType> getAllActiveDictionaryTypes() {
        return dictionaryService.getActiveDictionaryTypes();
    }

    @PostMapping()
    @AdminAccess
    public DictionaryType createDictionaryType(@RequestBody @Valid DictionaryTypeDto dictionaryTypeDto) {
        return dictionaryService.createDictionaryTypeWithTable(dictionaryTypeDto);
    }

    @DeleteMapping("/{dictionaryTypeName}")
    @AdminAccess
    public ResponseDto deleteDictionaryType(@PathVariable String dictionaryTypeName) {
        return dictionaryService.deleteDictionaryType(dictionaryTypeName);
    }
}
