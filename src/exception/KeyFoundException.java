package exception;

public class KeyFoundException extends RuntimeException {
    public KeyFoundException() {
        super("Ключ уже существует");
    }
}
