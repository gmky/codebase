package gmky.codebase.handler.builder;

import gmky.codebase.enumeration.EmailTypeEnum;
import gmky.codebase.model.event.ForgotPasswordEmailEvent;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class ForgotPasswordEmailHandlerTest {
    private static final String EMAIL = "admin@gmky.dev";
    @InjectMocks
    ForgotPasswordEmailBuilder builder;

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

    private ForgotPasswordEmailEvent mockEvent() {
        return ForgotPasswordEmailEvent.builder()
                .email(EMAIL)
                .emailType(EmailTypeEnum.FORGOT_PASSWORD)
                .build();
    }
}
