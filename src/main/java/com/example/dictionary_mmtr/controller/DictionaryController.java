package com.example.dictionary_mmtr.controller;

import com.example.dictionary_mmtr.annotation.AdminAccess;
import com.example.dictionary_mmtr.dto.*;
import com.example.dictionary_mmtr.service.BaseDictionaryService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/dictionaries")
@RequiredArgsConstructor
public class DictionaryController {

    private final BaseDictionaryService dictionaryService;

    @ModelAttribute("dictionaryType")
    public String getDictionaryType(@RequestParam(name = "dictionaryType", defaultValue = "latin") String dictionaryType) {
        return dictionaryType;
    }

    @GetMapping
    public KeyValuePairGroupDto getDictionary(@ModelAttribute DictionaryQueryDto query) {
        return dictionaryService.getDictionaryEntries(query);
    }

    @GetMapping(value = "/entries/export")
    public ResponseEntity<StreamingResponseBody> exportDictionaryToXml(
            @ModelAttribute("dictionaryType") String dictionaryType) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_XML)
                .body(outputStream -> dictionaryService.exportDictionaryToXml(dictionaryType, outputStream));
    }

    @GetMapping("/entries/search")
    public List<KeyValuePairDto> getDictionaryEntryByKey(
            @ModelAttribute("dictionaryType") String dictionaryType,
            @RequestParam(name = "key") String key) {
        return dictionaryService.findDictionaryEntryByKey(dictionaryType, key);
    }

    @PostMapping("/entries")
    @AdminAccess
    public KeyValuePairDto addDictionaryEntry(
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