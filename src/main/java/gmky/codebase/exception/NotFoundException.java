package gmky.codebase.exception;

import gmky.codebase.enumeration.ExceptionEnum;
import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseException {
    public NotFoundException(ExceptionEnum detail) {
        super(HttpStatus.NOT_FOUND, detail);
    }
}
