package gmky.codebase.service;

import gmky.codebase.enumeration.EmailTypeEnum;
import gmky.codebase.model.dto.SendEmailReq;
import gmky.codebase.service.impl.EmailServiceImpl;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {
    @Mock
    JavaMailSender mailSender;

    @Mock
    SpringTemplateEngine springTemplateEngine;

    @InjectMocks
    EmailServiceImpl emailService;

    @Mock
    MimeMessage message;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(emailService, "from", "codebase@gmky.deb");
        ReflectionTestUtils.setField(emailService, "name", "Codebase");
    }

    @Test
    @DisplayName("Test send mail should OK")
    void testSendMail_shouldOK() {
        var req = mockReq();
        Mockito.when(mailSender.createMimeMessage()).thenReturn(message);
        Mockito.when(springTemplateEngine.process(Mockito.any(String.class), Mockito.any(Context.class))).thenReturn("Email content");
        emailService.sendMailWithTemplate(req);
        Mockito.verify(mailSender, Mockito.times(1)).send(Mockito.any(MimeMessage.class));
    }

    SendEmailReq mockReq() {
        return SendEmailReq.builder()
                .to(List.of("admin@gmky.dev"))
                .subject(EmailTypeEnum.FORGOT_PASSWORD.getSubject())
                .template(EmailTypeEnum.FORGOT_PASSWORD.getTemplate())
                .context(Map.of())
                .build();
    }
}
