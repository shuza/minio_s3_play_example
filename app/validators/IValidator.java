package validators;

import java.util.ArrayList;

public interface IValidator {
    void validate();

    Object get();

    boolean hasErrors();

    ArrayList<String> getErrors();
}
