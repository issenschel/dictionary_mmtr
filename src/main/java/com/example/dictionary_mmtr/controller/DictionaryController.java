package com.example.dictionary_mmtr.controller;

import com.example.dictionary_mmtr.annotation.AdminAccess;
import com.example.dictionary_mmtr.dto.KeyValuePairGroupDto;
import com.example.dictionary_mmtr.dto.KeyValuePairRequestDto;
import com.example.dictionary_mmtr.dto.ResponseDto;
import com.example.dictionary_mmtr.entity.BaseDictionary;
import com.example.dictionary_mmtr.service.BaseDictionaryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
@RequestMapping("/dictionaries")
@RequiredArgsConstructor
public class DictionaryController {

    private final BaseDictionaryService dictionaryService;

    @ModelAttribute("dictionaryType")
    public String getDictionaryType(@RequestParam(name = "dictionaryType", defaultValue = "latin") String dictionaryType) {
        return dictionaryType;
    }

    @GetMapping("/entries")
    public KeyValuePairGroupDto getDictionaryEntries(
            @ModelAttribute("dictionaryType") String dictionaryType,
            @RequestParam(name = "page", defaultValue = "1") @Min(1) int page,
            @RequestParam(name = "size", defaultValue = "10") @Min(1) int size) {
        return dictionaryService.getDictionaryEntries(dictionaryType, page, size);
    }

    @GetMapping(value = "/entries/export", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<StreamingResponseBody> exportDictionaryToXml(
            @ModelAttribute("dictionaryType") String dictionaryType) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_XML)
                .body(outputStream -> dictionaryService.exportDictionaryToXml(dictionaryType, outputStream));
    }

    @GetMapping("/entries/search")
    public BaseDictionary getDictionaryEntryByKey(
            @ModelAttribute("dictionaryType") String dictionaryType,
            @RequestParam(name = "key") String key) {
        return dictionaryService.findDictionaryEntryByKey(dictionaryType, key);
    }

    @PostMapping("/entries")
    public BaseDictionary addDictionaryEntry(
            @ModelAttribute("dictionaryType") String dictionaryType,
            @RequestBody @Valid KeyValuePairRequestDto keyValuePairRequestDto) {
        return dictionaryService.addDictionaryEntry(dictionaryType, keyValuePairRequestDto);
    }

    @DeleteMapping("/entries")
    @AdminAccess
    public ResponseDto deleteDictionaryEntry(
            @ModelAttribute("dictionaryType") String dictionaryType,
            @RequestParam("key") String key) {
        return dictionaryService.removeDictionaryEntryByKey(dictionaryType, key);
    }
}