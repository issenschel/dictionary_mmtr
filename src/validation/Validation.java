package validation;

import java.util.function.Function;

public interface Validation {

    boolean validate(String word);

    String getRequirements();

    Function<String, String> getKeyTransformer();
}
