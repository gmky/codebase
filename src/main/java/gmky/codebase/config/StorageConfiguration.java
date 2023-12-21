package gmky.codebase.config;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import gmky.codebase.client.StorageService;
import gmky.codebase.client.impl.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class StorageConfiguration {
    @Bean
    public AmazonS3 s3Client() {
        return AmazonS3ClientBuilder.standard()
                .withRegion(Regions.AP_SOUTHEAST_1)
                .build();
    }

    @Bean
    public StorageService storageService() {
        log.info("Initializing Storage Service");
        return new S3Service(s3Client());
    }
}
