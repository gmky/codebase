package gmky.codebase.handler.builder;

import gmky.codebase.enumeration.EmailTypeEnum;
import gmky.codebase.model.dto.SendEmailReq;
import gmky.codebase.model.event.AccountActivationEmailEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class AccountActivationEmailBuilder implements EmailRequestBuilder<AccountActivationEmailEvent> {
    private static final String FULL_NAME_KEY = "fullName";

    @Override
    public SendEmailReq build(AccountActivationEmailEvent event) {
        return SendEmailReq.builder()
                .to(List.of(event.getEmail()))
                .template(event.getEmailType().getTemplate())
                .subject(event.getEmailType().getSubject())
                .context(Map.of(FULL_NAME_KEY, event.getFullName()))
                .build();
    }

    @Override
    public boolean isApplicable(EmailTypeEnum mailType) {
        return EmailTypeEnum.ACCOUNT_ACTIVATION.equals(mailType);
    }
}
