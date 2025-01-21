import menu.DictionarySelector;
import utils.ConfigLoader;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        new ConfigLoader();
        new DictionarySelector().selectDictionary();
    }
}