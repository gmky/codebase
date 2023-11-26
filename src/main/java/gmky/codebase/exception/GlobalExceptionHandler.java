package gmky.codebase.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(AccessDeniedException.class)
    ProblemDetail handleAccessDeniedException(AccessDeniedException exception, HttpServletRequest request) {
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, exception.getMessage());
        problemDetail.setType(URI.create(request.getRequestURI()));
        problemDetail.setDetail("Access Denied");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    @ExceptionHandler(BaseException.class)
    ResponseEntity<Map<String, Object>> handleResponseStatusException(BaseException exception, HttpServletRequest request) {
        var res = new HashMap<String, Object>();
        res.put("status", exception.getStatusCode().value());
        res.put("code", exception.getCode());
        res.put("message", exception.getMessage());
        res.put("timestamp", Instant.now());
        res.put("path", URI.create(request.getRequestURI()));
        return new ResponseEntity<>(res, exception.getStatusCode());
    }
}
