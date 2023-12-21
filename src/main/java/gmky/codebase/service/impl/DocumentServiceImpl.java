package gmky.codebase.service.impl;

import gmky.codebase.api.model.DocumentResponse;
import gmky.codebase.api.model.UploadFileReq;
import gmky.codebase.client.StorageService;
import gmky.codebase.enumeration.DocumentCategoryEnum;
import gmky.codebase.enumeration.DocumentStatusEnum;
import gmky.codebase.exception.NotFoundException;
import gmky.codebase.mapper.DocumentMapper;
import gmky.codebase.model.dto.ExtraInfoResource;
import gmky.codebase.repository.DocumentRepository;
import gmky.codebase.service.DocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static gmky.codebase.enumeration.ExceptionEnum.DOCUMENT_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;
    private final StorageService storageService;
    private final DocumentMapper documentMapper;

    @Override
    public Page<DocumentResponse> getAllDocuments(DocumentCategoryEnum category, DocumentStatusEnum status, Long refId, Pageable pageable) {
        var result = documentRepository.getAllDocuments(category, status, refId, pageable);
        return result.map(documentMapper::toDto);
    }

    @Override
    @Transactional
    public void uploadFiles(List<MultipartFile> files, UploadFileReq info) {
        files.forEach(file -> {
            var doc = documentMapper.toEntity(info);
            doc.setFileName(file.getOriginalFilename());
            doc.setFileType(file.getContentType());
            doc.setStatus(DocumentStatusEnum.ACTIVE);
            var fileKey = storageService.uploadFile(file, info);
            doc.setFilePath(fileKey);
            documentRepository.save(doc);
        });
    }

    @Override
    public DocumentResponse getDocumentById(Long docId) {
        var doc = documentRepository.findById(docId).orElseThrow(() -> new NotFoundException(DOCUMENT_NOT_FOUND));
        return documentMapper.toDto(doc);
    }

    @Override
    @Transactional
    public void deleteById(Long docId) {
        var doc = documentRepository.findById(docId).orElseThrow(() -> new NotFoundException(DOCUMENT_NOT_FOUND));
        documentRepository.deleteById(docId);
        storageService.deleteFile(doc.getFilePath());
    }

    @Override
    public ExtraInfoResource downloadById(Long docId) {
        var doc = documentRepository.findById(docId).orElseThrow(() -> new NotFoundException(DOCUMENT_NOT_FOUND));
        var data = storageService.downloadFile(doc.getFilePath());
        var resource = new ByteArrayResource(data);
        return new ExtraInfoResource(doc.getFileName(), resource);
    }
}
