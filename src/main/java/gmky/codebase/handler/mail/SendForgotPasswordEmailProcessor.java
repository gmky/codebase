package gmky.codebase.handler.mail;

import gmky.codebase.enumeration.MailTypeEnum;
import gmky.codebase.event.SendMailEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendForgotPasswordEmailProcessor implements SendMailProcessor {
    @Override
    public void handle(SendMailEvent event) {
        log.info("Process event: [{}]", event);
    }

    @Override
    public boolean isMatched(MailTypeEnum mailType) {
        return MailTypeEnum.FORGOT_PASSWORD.equals(mailType);
    }
}
