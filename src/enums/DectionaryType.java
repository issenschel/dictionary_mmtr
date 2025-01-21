package enums;

public enum DectionaryType {
    LATIN("default_latin"),
    NUMBER("default_numbers");

    private String description;

    DectionaryType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

