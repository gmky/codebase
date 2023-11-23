package gmky.codebase.handler.mail;

import gmky.codebase.enumeration.MailTypeEnum;
import gmky.codebase.event.SendMailEvent;

public interface SendMailProcessor {
    void handle(SendMailEvent event);

    boolean isMatched(MailTypeEnum type);
}
