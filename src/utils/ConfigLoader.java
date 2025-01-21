package utils;

import enums.DectionaryType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigLoader {

    public ConfigLoader() throws IOException {
        Properties props = new Properties();
        props.load(new FileInputStream("config/example.ini"));

        DectionaryType.LATIN.setDescription(props.getProperty("dictionary_latin"));
        DectionaryType.NUMBER.setDescription(props.getProperty("dictionary_number"));
    }

}
