package gmky.codebase.web.rest.v1;

import gmky.codebase.api.DocumentApi;
import gmky.codebase.api.model.DocumentResponse;
import gmky.codebase.api.model.UploadFileReq;
import gmky.codebase.enumeration.DocumentCategoryEnum;
import gmky.codebase.enumeration.DocumentStatusEnum;
import gmky.codebase.service.DocumentService;
import gmky.codebase.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DocumentResource implements DocumentApi {
    private final DocumentService documentService;

    @Override
    public ResponseEntity<Void> deleteById(Long documentId) {
        documentService.deleteById(documentId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Resource> downloadById(Long documentId) {
        var data = documentService.downloadById(documentId);
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", data.getFileName());
        return new ResponseEntity<>(data.getResource(), headers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<DocumentResponse>> getAllDocuments(DocumentCategoryEnum category, Long refId, DocumentStatusEnum status, Pageable pageable) {
        var page = documentService.getAllDocuments(category, status, refId, pageable);
        var headers = PaginationUtil.generatePaginationHeader(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<DocumentResponse> getDocumentDetail(Long documentId) {
        var result = documentService.getDocumentById(documentId);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Void> uploadDocuments(List<MultipartFile> files, UploadFileReq info) {
        documentService.uploadFiles(files, info);
        return ResponseEntity.noContent().build();
    }
}
