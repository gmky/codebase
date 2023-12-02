package gmky.codebase.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;

import static gmky.codebase.enumeration.ExceptionEnum.ACCESS_DENIED;
import static gmky.codebase.enumeration.ExceptionEnum.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;


@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String STATUS_KEY = "status";
    private static final String CODE_KEY = "code";
    private static final String MESSAGE_KEY = "message";
    private static final String TIME_KEY = "timestamp";
    private static final String PATH_KEY = "path";
    private static final String DETAIL_KEY = "detail";
    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<LinkedHashMap<String, Object>> handleAccessDeniedException(AccessDeniedException exception, HttpServletRequest request) {
        var res = buildExceptionResponse(FORBIDDEN.value(), ACCESS_DENIED.getCode(), ACCESS_DENIED.getMessage(), exception.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(res, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BaseException.class)
    ResponseEntity<LinkedHashMap<String, Object>> handleResponseStatusException(BaseException exception, HttpServletRequest request) {
        var res = buildExceptionResponse(exception.getStatusCode().value(), exception.getCode(), exception.getMessage(), null, request.getRequestURI());
        return new ResponseEntity<>(res, exception.getStatusCode());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var httpReq = ((ServletWebRequest) request).getRequest();
        var detail = buildDetailMessage(ex.getFieldErrors());
        var res = buildExceptionResponse(status.value(), BAD_REQUEST.getCode(), BAD_REQUEST.getMessage(), detail, httpReq.getRequestURI());
        return new ResponseEntity<>(res, headers, status);
    }

    private List<String> buildDetailMessage(List<FieldError> fieldErrors) {
        return fieldErrors.stream()
                .map(item -> String.format("%s %s", item.getField(), item.getDefaultMessage()))
                .toList();
    }

    private LinkedHashMap<String, Object> buildExceptionResponse(int status, String code, String msg, Object detail, String path) {
        var res = new LinkedHashMap<String, Object>();
        res.put(STATUS_KEY, status);
        res.put(CODE_KEY, code);
        res.put(MESSAGE_KEY, msg);
        res.put(PATH_KEY, URI.create(path));
        res.put(TIME_KEY, Instant.now());
        res.put(DETAIL_KEY, detail);
        return res;
    }
}
