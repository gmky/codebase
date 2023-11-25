package gmky.codebase.exception;

import gmky.codebase.enumeration.ExceptionEnum;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Getter
public class BaseException extends ResponseStatusException {
    private final String code;
    private final String message;

    public BaseException(HttpStatus httpStatus, ExceptionEnum detail) {
        super(httpStatus);
        this.code = detail.getCode();
        this.message = detail.getMessage();
    }
}
