package gmky.codebase.handler;

import gmky.codebase.enumeration.MailTypeEnum;
import gmky.codebase.event.SendMailEvent;
import gmky.codebase.handler.mail.SendMailProcessor;
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
public class SendMailEventHandler {
    private final List<SendMailProcessor> processors;

    @Async
    @EventListener(SendMailEvent.class)
    public void handle(SendMailEvent event) {
        log.info("Received event [{}]", event);
        var processor = getProcessor(event.getType());
        processor.handle(event);
    }

    private SendMailProcessor getProcessor(MailTypeEnum mailType) {
        return processors.stream()
                .filter(item -> item.isMatched(mailType))
                .findFirst()
                .orElseThrow(() -> new NotImplementedException("Mail type not implemented"));
    }
}
