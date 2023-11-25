package gmky.codebase.exception;

import gmky.codebase.enumeration.ExceptionEnum;
import org.springframework.http.HttpStatus;

public class BadRequestException extends BaseException {
    public BadRequestException(ExceptionEnum detail) {
        super(HttpStatus.BAD_REQUEST, detail);
    }
}
