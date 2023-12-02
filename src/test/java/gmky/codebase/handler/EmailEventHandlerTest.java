package gmky.codebase.handler;

import gmky.codebase.enumeration.EmailTypeEnum;
import gmky.codebase.handler.builder.EmailRequestBuilder;
import gmky.codebase.handler.builder.ForgotPasswordEmailBuilder;
import gmky.codebase.model.event.EmailEvent;
import gmky.codebase.model.event.EnvelopedEvent;
import gmky.codebase.model.event.ForgotPasswordEmailEvent;
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
        var event = ForgotPasswordEmailEvent.builder().email("admin@gmky.dev").emailType(EmailTypeEnum.FORGOT_PASSWORD).build();
        var envelopedEvent = new EnvelopedEvent<EmailEvent>(event);
        envelopedEvent.setEvent(event);
        Mockito.doNothing().when(emailService).sendMailWithTemplate(Mockito.any());
        emailEventHandler.handle(envelopedEvent);
        Mockito.verify(emailService, Mockito.times(1)).sendMailWithTemplate(Mockito.any());
    }

    @Test
    @DisplayName("Handle event should throw NotImplemented")
    void testHandleEvent_shouldThrowNotImplemented() {
        var event = EmailEvent.builder().build();
        var envelopedEvent = new EnvelopedEvent<>(event);
        envelopedEvent.setEvent(event);
        Assertions.assertThatThrownBy(() -> emailEventHandler.handle(envelopedEvent)).isInstanceOf(NotImplementedException.class);
    }
}