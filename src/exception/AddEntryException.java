package exception;

public class AddEntryException extends RuntimeException{
    public AddEntryException(){
        super("При добавлении значения произошла ошибка");
    }
}
