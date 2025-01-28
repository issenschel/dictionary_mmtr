package validation;

public class BackspaceValidation implements Validation{

    public boolean validate(String input) {
        return input.matches("[a-zA-Z#]*");
    }

    public String getRequirements(){
        return "Ключ должен быть латинскими буквами или содержать #";
    }
}
