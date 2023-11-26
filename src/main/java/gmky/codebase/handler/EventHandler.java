package gmky.codebase.handler;

public interface EventHandler<T> {
    void handle(T event);
}
