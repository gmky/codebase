package gmky.codebase.handler;

import gmky.codebase.enumeration.EmailTypeEnum;
import gmky.codebase.handler.builder.EmailRequestBuilder;
import gmky.codebase.handler.builder.ForgotPasswordEmailBuilder;
import gmky.codebase.model.event.EmailEvent;
import gmky.codebase.service.impl.EmailServiceImpl;
import org.apache.commons.lang3.NotImplementedException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class EmailEventHandlerTest {
    @Mock
    EmailServiceImpl emailService;

    @Spy
    ForgotPasswordEmailBuilder forgotPasswordEmailBuilder = new ForgotPasswordEmailBuilder();

    @Spy
    List<EmailRequestBuilder> builders = new ArrayList<>();

    @InjectMocks
    EmailEventHandler emailEventHandler;

    @BeforeEach
    void setUp() {
        builders.add(forgotPasswordEmailBuilder);
    }

    @Test
    @DisplayName("Handle event should OK")
    void testHandleEvent_shouldOK() {
        var event = new EmailEvent();
        event.setEmailType(EmailTypeEnum.FORGOT_PASSWORD);
        event.setParams(Map.of());
        Mockito.doNothing().when(emailService).sendMailWithTemplate(Mockito.any());
        emailEventHandler.handle(event);
        Mockito.verify(emailService, Mockito.times(1)).sendMailWithTemplate(Mockito.any());
    }

    @Test
    @DisplayName("Handle event should throw NotImplemented")
    void testHandleEvent_shouldThrowNotImplemented() {
        var event = new EmailEvent();
        event.setEmailType(null);
        Assertions.assertThatThrownBy(() -> emailEventHandler.handle(event)).isInstanceOf(NotImplementedException.class);
    }
}