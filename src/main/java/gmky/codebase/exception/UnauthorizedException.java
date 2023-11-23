package gmky.codebase.exception;

import gmky.codebase.enumeration.ExceptionEnum;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends BaseException {
    public UnauthorizedException(ExceptionEnum detail) {
        super(HttpStatus.UNAUTHORIZED, detail);
    }
}
