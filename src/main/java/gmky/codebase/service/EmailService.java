package gmky.codebase.service;

import gmky.codebase.model.dto.SendEmailReq;

public interface EmailService {
    void sendMailWithTemplate(SendEmailReq req);
}
