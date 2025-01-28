package menu;

import org.w3c.dom.Document;
import pojo.KeyValuePair;
import pojo.KeyValuePairGroup;
import service.DictionaryService;

import javax.swing.text.BadLocationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringWriter;
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
                               "6 - Вывод в формате XML\n" +
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
                case "6":
                    getDictionaryAsXML();
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
        try {
            List<KeyValuePair> keyValuePairs = dictionaryService.findAll();
            for (KeyValuePair keyValuePair : keyValuePairs) {
                System.out.println(keyValuePair.getKey() + " " + keyValuePair.getValue());
            }
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    private void pagination(){
        System.out.println("Введите страницу:");
        String page = console.nextLine();
        System.out.println("Введите количество элементов:");
        String size = console.nextLine();
        System.out.println("Список:");
        try {
            KeyValuePairGroup dictionaryPOJOList = dictionaryService.getPage(Integer.parseInt(page),Integer.parseInt(size));
            for (KeyValuePair keyValuePair : dictionaryPOJOList.getDictionary()) {
                System.out.println(keyValuePair.getKey() + " " + keyValuePair.getValue());
            }
            System.out.println("Количество страниц: " + dictionaryPOJOList.getCount());
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    private void searchEntryByKey(){
        System.out.println("Введите ключ для поиска");
        key = console.nextLine();
        try {
            KeyValuePair keyValuePair = dictionaryService.searchEntryByKey(key);
            System.out.println("Результат: " + keyValuePair.getKey() + " " + keyValuePair.getValue());
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    private void addEntry(){
        System.out.println("Введите ключ для добавления записи");
        key = console.nextLine();
        System.out.println("Введите значение перевода");
        String name= console.nextLine();
        try {
            KeyValuePair keyValuePair = dictionaryService.addEntry(key,name);
            System.out.println(keyValuePair.getKey() + " " + keyValuePair.getValue());
        }catch (RuntimeException e){
            System.out.println(e.getMessage());
        }
    }

    private void removeEntry(){
        System.out.print("Введите ключ для удаления записи: ");
        key = console.nextLine();
        try {
            System.out.println(dictionaryService.removeEntryByKey(key));
        }catch (RuntimeException e){
            System.out.println(e.getMessage());
        }
    }

    private void getDictionaryAsXML() {
        try(StringWriter stringWriter = new StringWriter()) {
            Document document = dictionaryService.getDictionaryAsXML();
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(stringWriter);
            transformer.transform(source, result);
            System.out.println(stringWriter);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


}
