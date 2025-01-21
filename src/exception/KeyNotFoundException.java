package exception;

public class KeyNotFoundException extends RuntimeException{
    public KeyNotFoundException(){
        super("Ключ не найден");
    }
}
