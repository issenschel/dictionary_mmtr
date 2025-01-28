package menu;

import pojo.KeyValuePair;
import pojo.KeyValuePairGroup;
import service.DictionaryService;

import java.util.List;
import java.util.Scanner;

public class DictionaryDisplay {

    private final Scanner console;
    private final DictionaryService dictionaryService;
    private String key;

    public DictionaryDisplay(Scanner console, DictionaryService dictionaryService) {
        this.console = console;
        this.dictionaryService = dictionaryService;
    }

    public void manageDictionary(){
        boolean exit = true;
        while (exit){
            System.out.println("Выберите что вы хотите сделать:\n" +
                               "1 - Список записей\n" +
                               "2 - Список записей пагинацией\n" +
                               "3 - Поиск записи\n" +
                               "4 - Добавление записи\n" +
                               "5 - Удаление записи\n" +
                               "0: Выход в выбор словаря");
            System.out.print("Введите число: ");
            String choice = console.nextLine();
            switch (choice) {
                case "1":
                    findAll();
                    break;
                case "2":
                    pagination();
                    break;
                case "3":
                    searchEntryByKey();
                    break;
                case "4":
                    addEntry();
                    break;
                case "5":
                    removeEntry();
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

    private void findAll(){
        System.out.println("Вот весь список:");
        List<KeyValuePair> keyValuePairs = dictionaryService.findAll();
        for (KeyValuePair keyValuePair : keyValuePairs) {
            System.out.println(keyValuePair.getKey() + " " + keyValuePair.getValue());
        }
    }

    private void pagination(){
        System.out.println("Введите страницу:");
        String page = console.nextLine();
        System.out.println("Введите количество элементов:");
        String size = console.nextLine();
        System.out.println("Список:");
        KeyValuePairGroup dictionaryPOJOList = dictionaryService.pagination(Integer.parseInt(page),Integer.parseInt(size));
        for (KeyValuePair keyValuePair : dictionaryPOJOList.getDictionary()) {
            System.out.println(keyValuePair.getKey() + " " + keyValuePair.getValue());
        }
        System.out.println("Количество страниц: " + dictionaryPOJOList.getCount());
    }

    private void searchEntryByKey(){
        System.out.println("Введите ключ для поиска");
        key = console.nextLine();
        KeyValuePair keyValuePair = dictionaryService.searchEntryByKey(key);
        if(keyValuePair.getKey() != null){
            System.out.println("Результат: " + keyValuePair.getKey() + " " + keyValuePair.getValue());
        }
    }

    private void addEntry(){
        System.out.println("Введите ключ для добавления записи");
        key = console.nextLine();
        System.out.println("Введите значение перевода");
        String name= console.nextLine();
        KeyValuePair keyValuePair = dictionaryService.addEntry(key,name);
        if(keyValuePair.getKey() != null){
            System.out.println(keyValuePair.getKey() + " " + keyValuePair.getValue());
        }
    }

    private void removeEntry(){
        System.out.print("Введите ключ для удаления записи: ");
        key = console.nextLine();
        System.out.println(dictionaryService.removeEntryByKey(key));
    }
}
