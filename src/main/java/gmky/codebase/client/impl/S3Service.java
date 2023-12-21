package gmky.codebase.client.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import gmky.codebase.api.model.UploadFileReq;
import gmky.codebase.client.StorageService;
import gmky.codebase.exception.InternalServerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;

import static gmky.codebase.enumeration.ExceptionEnum.UPLOAD_FILE_ERROR;

@Slf4j
@RequiredArgsConstructor
public class S3Service implements StorageService {
    private final AmazonS3 s3Client;
    @Value("${application.aws.s3.bucket-name}")
    private String bucketName;
    @Value("${application.aws.s3.prefix}")
    private String prefix;

    @Override
    public String uploadFile(MultipartFile file, UploadFileReq info) {
        try {
            log.debug("Starting upload file [{}] to bucket [{}]", file.getName(), bucketName);
            var key = buildKey(file.getOriginalFilename());
            s3Client.putObject(bucketName, key, file.getInputStream(), new ObjectMetadata());
            log.info("Completed uploading file to S3: [{}] with result key [{}]", info, key);
            return key;
        } catch (Exception ex) {
            log.error("Error when uploading file to S3", ex);
            throw new InternalServerException(UPLOAD_FILE_ERROR);
        }
    }

    @Override
    public void deleteFile(String key) {
        try {
            var deleteObjectReq = new DeleteObjectRequest(bucketName, key);
            s3Client.deleteObject(deleteObjectReq);
        } catch (Exception ex) {
            log.error("Error when delete object [{}]", key, ex);
            throw new InternalServerException(UPLOAD_FILE_ERROR);
        }
    }

    @Override
    public byte[] downloadFile(String key) {
        try {
            var getObjectReq = new GetObjectRequest(bucketName, key);
            var result = s3Client.getObject(getObjectReq);
            return result.getObjectContent().readAllBytes();
        } catch (Exception e) {
            log.error("Error when download file from S3: [{}]", key, e);
            throw new InternalServerException(UPLOAD_FILE_ERROR);
        }
    }

    private String buildKey(String fileName) {
        var now = Instant.now();
        return String.format("%s/%s_%s", prefix, now.toEpochMilli(), fileName);
    }
}
