package gmky.codebase.model.event;

import gmky.codebase.enumeration.EmailTypeEnum;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class EmailEvent {
    protected EmailTypeEnum emailType;
}
