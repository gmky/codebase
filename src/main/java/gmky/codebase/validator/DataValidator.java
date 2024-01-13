package gmky.codebase.validator;

import java.util.List;

public interface DataValidator {
    List<ValidationError> validate();

    default boolean isValid() {
        var errors = validate();
        return errors.isEmpty();
    }
}
