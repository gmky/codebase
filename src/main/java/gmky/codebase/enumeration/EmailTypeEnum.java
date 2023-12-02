package gmky.codebase.enumeration;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmailTypeEnum {
    FORGOT_PASSWORD("Forgot your password?", "forgot-password"),
    ACCOUNT_ACTIVATION("Activate your account", "account-activation");

    @NotNull
    private final String subject;
    @NotNull
    private final String template;
}
