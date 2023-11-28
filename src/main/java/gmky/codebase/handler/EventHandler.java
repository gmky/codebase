package gmky.codebase.handler;

import gmky.codebase.model.event.EnvelopedEvent;

@FunctionalInterface
public interface EventHandler<T> {
    void handle(EnvelopedEvent<T> event);
}
