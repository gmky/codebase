package gmky.codebase.model.event;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
public class AccountActivationEmailEvent extends EmailEvent {
    private String email;
    private String fullName;
}
