package gmky.codebase.model.event;

import gmky.codebase.enumeration.EmailTypeEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class EmailEvent extends BaseEvent {
    private EmailTypeEnum emailType;
    private Map<String, Object> params;
}
