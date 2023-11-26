package gmky.codebase.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class SendEmailReq {
    private List<String> to;
    private List<String> cc;
    private List<String> bcc;
    private String template;
    private String subject;
    private Map<String, Object> context;
}
