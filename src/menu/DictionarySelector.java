package menu;

import enums.DectionaryType;
import repository.FileDictionaryRepository;
import service.DictionaryService;
import service.FileDictionaryService;
import utils.ClearConsole;
import utils.ConfigLoader;
import validation.LatinValidation;
import validation.NumberValidation;
import validation.Validation;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class DictionarySelector {
    private final Scanner console = new Scanner(System.in);
    private final DictionaryService latinService;
    private final DictionaryService numberService;
    private DictionaryDisplay dictionaryDisplay;

    public DictionarySelector() {
        ConfigLoader configLoader = new ConfigLoader();

        latinService = createService(Paths.get(configLoader.getDictionaryValue(DectionaryType.LATIN)), new LatinValidation());
        numberService = createService(Paths.get(configLoader.getDictionaryValue(DectionaryType.NUMBER)), new NumberValidation());
    }

    private FileDictionaryService createService(Path dictionaryPath, Validation validation) {
        FileDictionaryRepository repository = new FileDictionaryRepository(dictionaryPath);
        return new FileDictionaryService(repository, validation);
    }

    public void selectDictionary() {
        boolean exit = true;
        while (exit){
            System.out.println("Приветствую в лучшем словаре!\n");
            System.out.println("Выберите словарь, с которым будете работать:\n" +
                               "1: Слово - перевод на русский\n" +
                               "2: Цифры - перевод на русский\n" +
                               "0: Завершение программы");
            System.out.print("Введите число: ");
            String choiceDictionary = console.nextLine();

            switch (choiceDictionary) {
                case "1":
                    setDictionaryDisplay(latinService);
                    ClearConsole.clear();
                    dictionaryDisplay.manageDictionary();
                    break;
                case "2":
                    setDictionaryDisplay(numberService);
                    ClearConsole.clear();
                    dictionaryDisplay.manageDictionary();
                    break;
                case "0":
                    exit = false;
                    break;
                default:
                    System.out.println("Такой операции нет ^_^");
                    break;
            }
        }
    }

    private void setDictionaryDisplay(DictionaryService service){
        this.dictionaryDisplay = new DictionaryDisplay(console,service);
    }
}
