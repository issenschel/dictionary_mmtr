package com.example.dictionary_mmtr.controller;

import com.example.dictionary_mmtr.dto.KeyValuePairDto;
import com.example.dictionary_mmtr.dto.KeyValuePairGroupDto;
import com.example.dictionary_mmtr.dto.KeyValuePairRequestDto;
import com.example.dictionary_mmtr.dto.ResponseDto;
import com.example.dictionary_mmtr.enums.DictionaryType;
import com.example.dictionary_mmtr.service.DictionaryFactoryService;
import jakarta.validation.constraints.Min;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
@RequestMapping("/dictionary")
public class DictionaryController {

    private final DictionaryFactoryService dictionaryFactoryService;


    public DictionaryController(DictionaryFactoryService dictionaryFactoryService) {
        this.dictionaryFactoryService = dictionaryFactoryService;
    }

    @ModelAttribute("dictionaryType")
    public DictionaryType getDictionaryType(@RequestParam(name = "dictionary", defaultValue = "LATIN") DictionaryType dictionaryType) {
        return dictionaryType;
    }


    @GetMapping()
    public KeyValuePairGroupDto getDictionary(@ModelAttribute("dictionaryType") DictionaryType dictionaryType,
                                              @RequestParam(name = "page", defaultValue = "1") @Min(1) int page,
                                              @RequestParam(name = "size", defaultValue = "1") @Min(1) int size) {
        return dictionaryFactoryService.getService(dictionaryType).getPage(page, size);
    }

    @GetMapping("/xml")
    public ResponseEntity<StreamingResponseBody> getDictionaryByXml(@ModelAttribute("dictionaryType") DictionaryType dictionaryType) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_XML)
                .body(outputStream -> dictionaryFactoryService.getService(dictionaryType).getDictionaryAsXML(outputStream));
    }


    @GetMapping("/find")
    public KeyValuePairDto getEntryByKey(@ModelAttribute("dictionaryType") DictionaryType dictionaryType,
                                         @RequestParam("key") String key) {
        return dictionaryFactoryService.getService(dictionaryType).searchEntryByKey(key);
    }


    @PostMapping()
    public KeyValuePairDto addEntry(@ModelAttribute("dictionaryType") DictionaryType dictionaryType,
                                    @RequestBody KeyValuePairRequestDto keyValuePairRequestDto) {
        return dictionaryFactoryService.getService(dictionaryType).addEntry(keyValuePairRequestDto.getKey(), keyValuePairRequestDto.getValue());
    }


    @DeleteMapping()
    public ResponseDto deleteEntry(@ModelAttribute("dictionaryType") DictionaryType dictionaryType,
                                   @RequestParam("key") String key) {
        return dictionaryFactoryService.getService(dictionaryType).removeEntryByKey(key);
    }


}
