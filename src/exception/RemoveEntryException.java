package exception;

public class RemoveEntryException extends RuntimeException{
    public RemoveEntryException(String message){
        super("При удалении записи что-то пошло не так" + message);
    }

}
