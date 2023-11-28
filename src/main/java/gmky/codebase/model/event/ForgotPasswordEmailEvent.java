package gmky.codebase.model.event;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class ForgotPasswordEmailEvent extends EmailEvent {
    private Long userId;

    private String username;

    private String email;

    private String fullName;
}
