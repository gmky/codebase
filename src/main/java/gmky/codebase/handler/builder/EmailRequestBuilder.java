package gmky.codebase.handler.builder;

import gmky.codebase.enumeration.EmailTypeEnum;
import gmky.codebase.model.dto.SendEmailReq;
import gmky.codebase.model.event.EmailEvent;

public interface EmailRequestBuilder {
    SendEmailReq build(EmailEvent event);

    boolean isApplicable(EmailTypeEnum mailType);
}
