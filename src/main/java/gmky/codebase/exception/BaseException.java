package gmky.codebase.exception;

import gmky.codebase.enumeration.ExceptionEnum;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

@Getter
public class BaseException extends ResponseStatusException {
    private final String code;
    private final String message;

    public BaseException(HttpStatusCode status, ExceptionEnum detail) {
        super(status, detail.getMessage());
        this.code = detail.getCode();
        this.message = detail.getMessage();
    }
}
