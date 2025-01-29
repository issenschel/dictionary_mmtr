package validation;

import java.util.function.Function;

public class LatinValidation implements Validation {

    public boolean validate(String input) {
        return input.matches("[a-zA-Z]+") && input.length() == 4;
    }

    public String getRequirements(){
        return "Ключ должен быть латинскими буквами. Длина должна быть равной 4";
    }

    public Function<String, String> getKeyTransformer(){
        return key -> key;
    }
}
