package gmky.codebase.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {
    private Security security;

    @Getter
    @Setter
    public static class Security {
        private String base64Secret;
    }
}
