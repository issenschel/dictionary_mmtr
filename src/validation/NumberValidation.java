package validation;

public class NumberValidation implements Validation {
    public boolean validate(String input){
        return input.matches("[0-9]+") && input.length() == 5;
    }

    public String getRequirements(){
        return "Ключ должен быть цифрами. Длина должна быть равной 5";
    }
}
