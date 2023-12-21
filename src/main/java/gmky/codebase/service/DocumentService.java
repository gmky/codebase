package gmky.codebase.service;

import gmky.codebase.api.model.DocumentResponse;
import gmky.codebase.api.model.UploadFileReq;
import gmky.codebase.enumeration.DocumentCategoryEnum;
import gmky.codebase.enumeration.DocumentStatusEnum;
import gmky.codebase.model.dto.ExtraInfoResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentService {
    Page<DocumentResponse> getAllDocuments(DocumentCategoryEnum category, DocumentStatusEnum status, Long refId, Pageable pageable);

    void uploadFiles(List<MultipartFile> files, UploadFileReq info);

    DocumentResponse getDocumentById(Long docId);

    void deleteById(Long docId);

    ExtraInfoResource downloadById(Long docId);
}
