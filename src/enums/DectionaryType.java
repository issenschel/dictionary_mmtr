package enums;

public enum DectionaryType {
    LATIN("dictionary_latin"),
    NUMBER("dictionary_number");

    private final String description;

    DectionaryType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

