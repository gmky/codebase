package gmky.codebase.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionEnum {
    ACCESS_DENIED("CB001", "Access denied"),
    BAD_REQUEST("CB002", "Bad request"),
    USERNAME_NOT_FOUND("CB003", "Username not found"),
    LOGIN_INFO_NOT_MATCH("CB004", "Username and password does not match"),
    JOB_ROLE_NOT_FOUND("CB005", "Job role not found"),
    USERNAME_EXISTED("CB006", "Username already existed"),
    EMAIL_EXISTED("CB007", "Email already existed"),
    USER_NOT_FOUND("CB008", "User not found"),
    INVALID_STATUS("CB009", "User status invalid");

    private final String code;
    private final String message;
}
