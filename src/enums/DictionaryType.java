package enums;

public enum DictionaryType {
    LATIN("dictionary_latin"),
    NUMBER("dictionary_number"),
    BACKSPACE("dictionary_backspace");

    private final String description;

    DictionaryType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

