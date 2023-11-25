package gmky.codebase.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionEnum {
    ACCESS_DENIED("CB001", "Access denied"),
    USERNAME_NOT_FOUND("CB002", "Username not found"),
    LOGIN_INFO_NOT_MATCH("CB003", "Username and password does not match"),
    JOB_ROLE_NOT_FOUND("CB004", "Job role not found"),
    USERNAME_EXISTED("CB005", "Username already existed"),
    EMAIL_EXISTED("CB006", "Email already existed"),
    USER_NOT_FOUND("CB007", "User not found");

    private final String code;
    private final String message;
}
