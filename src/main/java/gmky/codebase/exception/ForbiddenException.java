package gmky.codebase.exception;

import gmky.codebase.enumeration.ExceptionEnum;
import org.springframework.http.HttpStatus;

public class ForbiddenException extends BaseException {
    public ForbiddenException(ExceptionEnum detail) {
        super(HttpStatus.FORBIDDEN, detail);
    }
}
