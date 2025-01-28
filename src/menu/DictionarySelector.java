package menu;

import enums.DictionaryType;
import service.*;
import service.DictionaryFactoryService;
import utils.ClearConsole;

import java.util.Scanner;

public class DictionarySelector {
    private final Scanner console = new Scanner(System.in);
    private final DictionaryService latinService;
    private final DictionaryService numberService;
    private final DictionaryService backspaceService;
    private DictionaryDisplay dictionaryDisplay;

    public DictionarySelector() {
        DictionaryFactoryService serviceFactory = new DictionaryFactoryService();
        latinService = serviceFactory.createService(DictionaryType.LATIN);
        numberService = serviceFactory.createService(DictionaryType.NUMBER);
        backspaceService = serviceFactory.createService(DictionaryType.BACKSPACE);
    }


    public void selectDictionary() {
        boolean exit = true;
        while (exit){
            System.out.println("Приветствую в лучшем словаре!\n");
            System.out.println("Выберите словарь, с которым будете работать:\n" +
                               "1: Слово - перевод на русский\n" +
                               "2: Цифры - перевод на русский\n" +
                               "3: Слово - перевод на русский. Включён знак # - backspace\n" +
                               "0: Завершение программы");
            System.out.print("Введите число: ");
            String choiceDictionary = console.nextLine();

            switch (choiceDictionary) {
                case "1":
                    manageDictionary(latinService);
                    break;
                case "2":
                    manageDictionary(numberService);
                    break;
                case "3":
                    manageDictionary(backspaceService);
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

    private void manageDictionary(DictionaryService service) {
        setDictionaryDisplay(service);
        ClearConsole.clear();
        dictionaryDisplay.manageDictionary();
    }

    private void setDictionaryDisplay(DictionaryService service) {
        this.dictionaryDisplay = new DictionaryDisplay(console, service);
    }
}
