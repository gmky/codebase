package gmky.codebase.service.impl;

import gmky.codebase.model.dto.SendEmailReq;
import gmky.codebase.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final SpringTemplateEngine templateEngine;
    private final JavaMailSender mailSender;
    @Value("${spring.mail.properties.from}")
    private String from;
    @Value("${spring.mail.properties.name}")
    private String name;
    @Value("${spring.mail.properties.signature}")
    private String signature;

    @Override
    public void sendMailWithTemplate(SendEmailReq req) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            var msgHelper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
            msgHelper.setFrom(from, name);
            msgHelper.setSubject(req.getSubject());
            msgHelper.setTo(toArray(req.getTo()));
            msgHelper.setCc(toArray(req.getCc()));
            msgHelper.setBcc(toArray(req.getBcc()));
            var content = templateEngine.process(req.getTemplate(), buildContext(req.getContext()));
            msgHelper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("Unable to create message helper", e);
        } catch (UnsupportedEncodingException e) {
            log.error("Unable to set email from", e);
        }
    }

    private Context buildContext(Map<String, Object> context) {
        var result = new Context();
        result.setVariables(context);
        result.setVariable("signature", signature);
        return result;
    }

    private String[] toArray(List<String> list) {
        if (list == null) return new String[0];
        return list.toArray(String[]::new);
    }
}
