package gmky.codebase.handler.builder;

import gmky.codebase.enumeration.EmailTypeEnum;
import gmky.codebase.model.dto.SendEmailReq;
import gmky.codebase.model.event.ForgotPasswordEmailEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class ForgotPasswordEmailBuilder implements EmailRequestBuilder<ForgotPasswordEmailEvent> {
    private static final String EMAIL_KEY = "email";
    private static final String FULL_NAME_KEY = "fullName";
    @Override
    public SendEmailReq build(ForgotPasswordEmailEvent event) {
        var context = new HashMap<String, Object>();
        context.put(EMAIL_KEY, event.getEmail());
        context.put(FULL_NAME_KEY, event.getFullName());
        return SendEmailReq.builder()
                .subject(event.getEmailType().getSubject())
                .template(event.getEmailType().getTemplate())
                .context(context)
                .to(List.of(event.getEmail()))
                .build();
    }

    @Override
    public boolean isApplicable(EmailTypeEnum mailType) {
        return EmailTypeEnum.FORGOT_PASSWORD.equals(mailType);
    }
}
