package gmky.codebase.client;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectResult;
import gmky.codebase.api.model.UploadFileReq;
import gmky.codebase.client.impl.S3Service;
import gmky.codebase.enumeration.DocumentCategoryEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;

@ExtendWith(MockitoExtension.class)
class S3ServiceTest {
    @Spy
    AmazonS3 s3Client = Mockito.mock(AmazonS3.class);

    @Spy
    S3Service s3Service = new S3Service(s3Client);

    @Test
    @DisplayName("Upload file should OK")
    void testUploadFile_shouldOK() {
        long expectedEpochSecond = 123456789L;
        var instant = Instant.ofEpochMilli(expectedEpochSecond);
        try (MockedStatic<Instant> instantMockedStatic = Mockito.mockStatic(Instant.class, Mockito.CALLS_REAL_METHODS)) {
            ReflectionTestUtils.setField(s3Service, "bucketName", "gmky");
            ReflectionTestUtils.setField(s3Service, "prefix", "codebase");
            instantMockedStatic.when(Instant::now).thenReturn(instant);
            var file = Mockito.mock(MultipartFile.class);
            var req = new UploadFileReq();
            req.setCategory(DocumentCategoryEnum.TEMPLATE);
            Mockito.when(s3Client.putObject(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(new PutObjectResult());
            var result = s3Service.uploadFile(file, req);
            Assertions.assertNotNull(result);
        }
    }
}
