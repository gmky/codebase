package gmky.codebase.handler;

import gmky.codebase.handler.builder.EmailRequestBuilder;
import gmky.codebase.model.event.EmailEvent;
import gmky.codebase.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailEventHandler implements EventHandler<EmailEvent> {
    private final List<EmailRequestBuilder> builders;
    private final EmailService emailService;

    @Async
    @Override
    @EventListener(EmailEvent.class)
    public void handle(EmailEvent event) {
        log.info("Received email event: {}", event.getId());
        var builder = getBuilder(event);
        var emailReq = builder.build(event);
        emailService.sendMailWithTemplate(emailReq);
    }

    private EmailRequestBuilder getBuilder(EmailEvent event) {
        return builders.stream().filter(item -> item.isApplicable(event.getEmailType()))
                .findFirst()
                .orElseThrow(() -> new NotImplementedException("No email builder was found"));
    }
}
