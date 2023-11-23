package gmky.codebase.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionEnum {
    ACCESS_DENIED("AUTHORIZATION_001", "Access Denied"),

    USER_NOT_FOUND("USER_001", "User not found"),
    USER_EMAIL_NOT_FOUND("USER_002", "Email address not found"),
    USER_USERNAME_EXISTED("USER_003", "Username already existed"),
    USER_EMAIL_EXISTED("USER_004", "Email already existed"),

    AUTH_INVALID_DATA("AUTH_001", "Username and password not match"),

    JOB_ROLE_NOT_FOUND("JOB_ROLE_001", "Job role not found");

    private final String code;
    private final String message;
}
