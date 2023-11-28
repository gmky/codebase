package gmky.codebase.model.event;

import gmky.codebase.enumeration.EmailTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class EmailEvent {
    protected EmailTypeEnum emailType;
}
