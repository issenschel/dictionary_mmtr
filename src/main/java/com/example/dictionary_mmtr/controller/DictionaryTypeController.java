package com.example.dictionary_mmtr.controller;

import com.example.dictionary_mmtr.dto.DictionaryTypeRequest;
import com.example.dictionary_mmtr.entity.DictionaryType;
import com.example.dictionary_mmtr.service.DictionaryTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @PostMapping
    public DictionaryType createDictionaryType(@Valid @RequestBody DictionaryTypeRequest dictionaryTypeRequest) {
        return dictionaryTypeService.createDictionaryType(dictionaryTypeRequest);
    }

    @DeleteMapping("/{dictionaryTypeName}")
    public void deleteDictionaryType(@PathVariable String dictionaryTypeName) {
        dictionaryTypeService.deleteDictionaryType(dictionaryTypeName);
    }
}
