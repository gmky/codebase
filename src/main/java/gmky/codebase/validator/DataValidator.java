package gmky.codebase.validator;

import java.util.List;

public interface DataValidator {
    List<ValidationError> validate();
}
