package gmky.codebase.service;

import gmky.codebase.api.model.DocumentResponse;
import gmky.codebase.api.model.UploadFileReq;
import gmky.codebase.client.StorageService;
import gmky.codebase.enumeration.DocumentCategoryEnum;
import gmky.codebase.enumeration.DocumentStatusEnum;
import gmky.codebase.exception.NotFoundException;
import gmky.codebase.mapper.DocumentMapper;
import gmky.codebase.model.entity.Document;
import gmky.codebase.repository.DocumentRepository;
import gmky.codebase.service.impl.DocumentServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {
    private static final String DOCUMENT_KEY = "/codebase/abc.txt";
    private static final Long DOCUMENT_ID = 1L;
    @Mock
    DocumentRepository documentRepository;

    @Mock
    StorageService storageService;

    @Mock
    DocumentMapper documentMapper;

    @InjectMocks
    DocumentServiceImpl documentService;

    @Test
    @DisplayName("Get all documents should OK")
    void testGetAllDocuments_shouldOK() {
        Mockito.when(documentRepository.getAllDocuments(DocumentCategoryEnum.TEMPLATE, DocumentStatusEnum.ACTIVE, null, PageRequest.of(0, 20)))
                .thenReturn(new PageImpl<>(List.of()));
        var data = documentService.getAllDocuments(DocumentCategoryEnum.TEMPLATE, DocumentStatusEnum.ACTIVE, null, PageRequest.of(0, 20));
        Assertions.assertNotNull(data);
    }

    @Test
    @DisplayName("Upload files should OK")
    void testUploadFile_shouldOK() {
        var file = Mockito.mock(MultipartFile.class);
        var info = new UploadFileReq();
        var doc = new Document();
        Mockito.when(storageService.uploadFile(file, info)).thenReturn("/path/to/file");
        Mockito.when(documentMapper.toEntity(info)).thenReturn(doc);
        Mockito.when(documentRepository.save(Mockito.any(Document.class))).thenReturn(doc);
        documentService.uploadFiles(List.of(file), info);
        Mockito.verify(storageService, Mockito.times(1)).uploadFile(file, info);
    }

    @Test
    @DisplayName("Delete document by ID")
    void testDeleteDocumentById_shouldOK() {
        var doc = mockDoc();
        Mockito.doNothing().when(storageService).deleteFile(DOCUMENT_KEY);
        Mockito.when(documentRepository.findById(DOCUMENT_ID)).thenReturn(Optional.of(doc));
        Mockito.doNothing().when(documentRepository).deleteById(DOCUMENT_ID);
        documentService.deleteById(DOCUMENT_ID);
        Mockito.verify(storageService, Mockito.times(1)).deleteFile(DOCUMENT_KEY);
    }

    @Test
    @DisplayName("Delete document by ID should throw NotFoundException")
    void testDeleteByDocumentById_shouldThrowNotFoundException() {
        Mockito.when(documentRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> documentService.deleteById(1L));
    }

    @Test
    @DisplayName("Download document by ID should throw NotFoundException")
    void testDownloadDocumentByID_shouldThrowNotFoundException() {
        Mockito.when(documentRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> documentService.downloadById(1L));
    }

    @Test
    @DisplayName("Download document by ID should OK")
    void testDownloadDocumentById_shouldOK() {
        var doc = mockDoc();
        Mockito.when(storageService.downloadFile(DOCUMENT_KEY)).thenReturn(DOCUMENT_KEY.getBytes(Charset.defaultCharset()));
        Mockito.when(documentRepository.findById(DOCUMENT_ID)).thenReturn(Optional.of(doc));
        var result = documentService.downloadById(DOCUMENT_ID);
        Assertions.assertNotNull(result);
    }

    @Test
    @DisplayName("Get document by ID should OK")
    void testGetDocumentById_shouldOK() {
        var doc = mockDoc();
        Mockito.when(documentRepository.findById(DOCUMENT_ID)).thenReturn(Optional.of(doc));
        Mockito.when(documentMapper.toDto(doc)).thenReturn(new DocumentResponse());
        var result = documentService.getDocumentById(DOCUMENT_ID);
        Assertions.assertNotNull(result);
    }

    @Test
    @DisplayName("Get document by ID should throw NotFoundException")
    void testGetDocumentById_shouldThrowNotFoundException() {
        Mockito.when(documentRepository.findById(DOCUMENT_ID)).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> documentService.getDocumentById(DOCUMENT_ID));
    }

    private Document mockDoc() {
        var doc = new Document();
        doc.setId(DOCUMENT_ID);
        doc.setFilePath(DOCUMENT_KEY);
        return doc;
    }
}
