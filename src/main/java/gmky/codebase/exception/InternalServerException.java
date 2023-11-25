package gmky.codebase.exception;

import gmky.codebase.enumeration.ExceptionEnum;
import org.springframework.http.HttpStatus;

public class InternalServerException extends BaseException {
    public InternalServerException(ExceptionEnum detail) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, detail);
    }
}
