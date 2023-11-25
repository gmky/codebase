package gmky.codebase.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.nio.charset.StandardCharsets;

@Configuration
public class TemplateEngineConfiguration {
    private static final String EMAIL_TEMPLATE_PREFIX = "/template/email/";
    private static final String EMAIL_TEMPLATE_SUFFIX = ".html";

    @Bean
    public SpringTemplateEngine emailTemplateEngine() {
        final var springTemplateEngine = new SpringTemplateEngine();
        springTemplateEngine.addTemplateResolver(emailTemplateResolver());
        return springTemplateEngine;
    }

    private ClassLoaderTemplateResolver emailTemplateResolver() {
        final var resolver = new ClassLoaderTemplateResolver();
        resolver.setCacheable(false);
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resolver.setPrefix(EMAIL_TEMPLATE_PREFIX);
        resolver.setSuffix(EMAIL_TEMPLATE_SUFFIX);
        return resolver;
    }
}
