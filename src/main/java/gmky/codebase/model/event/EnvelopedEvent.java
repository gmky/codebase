package gmky.codebase.model.event;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class EnvelopedEvent<T> {
    protected final UUID id = UUID.randomUUID();
    protected T event;

    public EnvelopedEvent(T event) {
        this.event = event;
    }
}
