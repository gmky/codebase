package gmky.codebase.model.event;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class BaseEvent {
    protected UUID id;

    public BaseEvent() {
        this.id = UUID.randomUUID();
    }
}
