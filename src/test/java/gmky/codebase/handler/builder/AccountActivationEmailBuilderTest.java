package gmky.codebase.handler.builder;

import gmky.codebase.enumeration.EmailTypeEnum;
import gmky.codebase.model.event.AccountActivationEmailEvent;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class AccountActivationEmailBuilderTest {
    private static final String EMAIL = "admin@gmky.dev";
    private static final String FULL_NAME = "Vu Hoang Hiep";
    @InjectMocks
    AccountActivationEmailBuilder builder;

    @Test
    @DisplayName("Test build should OK")
    void testBuild_shouldOK() {
        var event = mockEvent();
        var result = builder.build(event);
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getTo()).isEqualTo(List.of(EMAIL));
    }

    @Test
    @DisplayName("Test check applicable is OK")
    void testIsApplicable_shouldReturnTrue() {
        var event = mockEvent();
        var result = builder.isApplicable(event.getEmailType());
        Assertions.assertThat(result).isTrue();
    }

    private AccountActivationEmailEvent mockEvent() {
        return AccountActivationEmailEvent.builder()
                .email(EMAIL)
                .fullName(FULL_NAME)
                .emailType(EmailTypeEnum.ACCOUNT_ACTIVATION)
                .build();
    }
}
