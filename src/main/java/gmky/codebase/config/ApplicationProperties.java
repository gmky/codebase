package gmky.codebase.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {
    private Security security;
    private AWS aws;

    @Getter
    @Setter
    public static class Security {
        private String base64Secret;
    }

    @Getter
    @Setter
    public static class AWS {
        private S3 s3;

        @Getter
        @Setter
        public static class S3 {
            private String bucketName;
            private String prefix;
        }
    }
}
