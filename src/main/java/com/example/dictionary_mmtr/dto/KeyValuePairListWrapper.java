package com.example.dictionary_mmtr.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public class KeyValuePairListWrapper {
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "entry")
    private List<KeyValuePairDto> entries;

    public KeyValuePairListWrapper(List<KeyValuePairDto> entries) {
        this.entries = entries;
    }

    public List<KeyValuePairDto> getEntries() {
        return entries;
    }

    public void setEntries(List<KeyValuePairDto> entries) {
        this.entries = entries;
    }
}
