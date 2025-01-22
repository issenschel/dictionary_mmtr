package menu;

import dto.DictionaryDto;
import service.DictionaryService;

import java.util.List;
import java.util.Scanner;

public class DictionaryDisplay {

    private final Scanner console;
    private final DictionaryService dictionaryService;

    public DictionaryDisplay(Scanner console, DictionaryService dictionaryService) {
        this.console = console;
        this.dictionaryService = dictionaryService;
    }

    public void manageDictionary(){
        boolean exit = true;
        while (exit){
            String key;
            System.out.println("Выберите что вы хотите сделать:\n" +
                               "1 - Список записей\n" +
                               "2 - Удаление записи\n" +
                               "3 - Поиск записи\n" +
                               "4 - Добавление записи\n" +
                               "0: Выход в выбор словаря");
            System.out.print("Введите число: ");
            String choice = console.nextLine();
            switch (choice) {
                case "1":
                    System.out.println("Вот весь список:");
                    List<DictionaryDto> dictionaryDtos = dictionaryService.findAll();
                    for (DictionaryDto dictionaryDto : dictionaryDtos) {
                        System.out.println(dictionaryDto.getKey() + " " + dictionaryDto.getValue());
                    }
                    break;
                case "2":
                    System.out.print("Введите ключ для удаления записи: ");
                    key = console.nextLine();
                    dictionaryService.removeEntryByKey(key);
                    break;
                case "3":
                    System.out.println("Введите ключ для поиска");
                    key = console.nextLine();
                    dictionaryService.searchEntryByKey(key);
                    break;
                case "4":
                    System.out.println("Введите ключ для добавления записи");
                    key = console.nextLine();
                    System.out.println("Введите значение перевода");
                    String name= console.nextLine();
                    dictionaryService.addEntry(key, name);
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
}
