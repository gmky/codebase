package gmky.codebase.event;

import gmky.codebase.enumeration.MailTypeEnum;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class SendMailEvent implements Serializable {
    private MailTypeEnum type;
    private List<String> toAddresses;
    private List<String> ccAddresses;
    private List<String> bccAddresses;
}
