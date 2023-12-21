package gmky.codebase.client;

import com.amazonaws.SdkBaseException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectsResult;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import gmky.codebase.api.model.UploadFileReq;
import gmky.codebase.client.impl.S3Service;
import gmky.codebase.enumeration.DocumentCategoryEnum;
import gmky.codebase.exception.InternalServerException;
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

import java.io.ByteArrayInputStream;
import java.time.Instant;
import java.util.List;

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

    @Test
    @DisplayName("Upload file should throw InternalServerException")
    void testUploadFile_shouldThrowInternalServerException() {
        long expectedEpochSecond = 123456789L;
        var instant = Instant.ofEpochMilli(expectedEpochSecond);
        try (MockedStatic<Instant> instantMockedStatic = Mockito.mockStatic(Instant.class, Mockito.CALLS_REAL_METHODS)) {
            ReflectionTestUtils.setField(s3Service, "bucketName", "gmky");
            ReflectionTestUtils.setField(s3Service, "prefix", "codebase");
            instantMockedStatic.when(Instant::now).thenReturn(instant);
            var file = Mockito.mock(MultipartFile.class);
            var req = new UploadFileReq();
            req.setCategory(DocumentCategoryEnum.TEMPLATE);
            Mockito.when(s3Client.putObject(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                    .thenThrow(new RuntimeException("Not ok"));
            Assertions.assertThrows(InternalServerException.class, () -> s3Service.uploadFile(file, req));
        }
    }

    @Test
    @DisplayName("Delete file should OK")
    void testDeleteFile_shouldOK() {
        Mockito.when(s3Client.deleteObjects(Mockito.any())).thenReturn(new DeleteObjectsResult(List.of()));
        s3Service.deleteFile("abc");
        Mockito.verify(s3Client, Mockito.times(1)).deleteObject(Mockito.any());
    }

    @Test
    @DisplayName("Delete file should throw InternalServerException")
    void testDeleteFile_shouldThrowInternalServerException() {
        Mockito.doThrow(new SdkBaseException("Error")).when(s3Client).deleteObject(Mockito.any());
        Assertions.assertThrows(InternalServerException.class, () -> s3Service.deleteFile("abc"));
    }

    @Test
    @DisplayName("Download file should OK")
    void testDownloadFile_shouldOK() {
        var s3Result = new S3Object();
        s3Result.setObjectContent(new ByteArrayInputStream("abc".getBytes()));
        Mockito.when(s3Client.getObject(Mockito.any(GetObjectRequest.class)))
                .thenReturn(s3Result);
        var result = s3Service.downloadFile("abc");
        Assertions.assertNotNull(result);
    }

    @Test
    @DisplayName("Download file should throw InternalServerException")
    void testDownload_shouldThrowInternalServerException() {
        Mockito.when(s3Client.getObject(Mockito.any(GetObjectRequest.class)))
                .thenThrow(new SdkBaseException("Error"));
        Assertions.assertThrows(InternalServerException.class, () -> s3Service.downloadFile("abc"));
    }
}
