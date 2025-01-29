import menu.DictionarySelector;
import utils.ConfigLoader;

public class Main {
    public static void main(String[] args) {
        new ConfigLoader();
        new DictionarySelector().selectDictionary();
    }
}