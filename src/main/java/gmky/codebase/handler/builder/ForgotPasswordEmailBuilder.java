package gmky.codebase.handler.builder;

import gmky.codebase.enumeration.EmailTypeEnum;
import gmky.codebase.model.dto.SendEmailReq;
import gmky.codebase.model.event.EmailEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ForgotPasswordEmailBuilder implements EmailRequestBuilder {
    public static final String EMAIL_KEY = "email";
    public static final String FULL_NAME_KEY = "fullName";

    @Override
    public SendEmailReq build(EmailEvent event) {
        var params = event.getParams();
        var email = String.valueOf(params.get(EMAIL_KEY));
        return SendEmailReq.builder()
                .subject(event.getEmailType().getSubject())
                .template(event.getEmailType().getTemplate())
                .context(event.getParams())
                .to(List.of(email))
                .build();
    }

    @Override
    public boolean isApplicable(EmailTypeEnum mailType) {
        return EmailTypeEnum.FORGOT_PASSWORD.equals(mailType);
    }
}
