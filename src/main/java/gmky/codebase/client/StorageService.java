package gmky.codebase.client;

import gmky.codebase.api.model.UploadFileReq;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String uploadFile(MultipartFile file, UploadFileReq info);

    void deleteFile(String key);

    byte[] downloadFile(String key);
}
