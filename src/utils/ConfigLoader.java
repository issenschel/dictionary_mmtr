package utils;

import enums.DectionaryType;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigLoader {
    private final Properties props;

    public ConfigLoader() {
        props = new Properties();
        try {
            props.load(new FileInputStream("config/example.ini"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getDictionaryValue(DectionaryType type) {
        return props.getProperty(type.getDescription());
    }
}

