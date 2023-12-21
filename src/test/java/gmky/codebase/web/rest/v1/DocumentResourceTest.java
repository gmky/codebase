package gmky.codebase.web.rest.v1;

import gmky.codebase.api.model.DocumentResponse;
import gmky.codebase.enumeration.DocumentCategoryEnum;
import gmky.codebase.enumeration.DocumentStatusEnum;
import gmky.codebase.model.dto.ExtraInfoResource;
import gmky.codebase.service.impl.DocumentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
class DocumentResourceTest {
    private static final String GET_ALL_DOCUMENTS_URI = "/client-api/v1/documents";
    private static final String GET_DOCUMENT_BY_ID_URI = "/client-api/v1/documents/1";
    private static final String UPLOAD_DOCUMENT_URI = "/client-api/v1/documents";
    private static final String DOWNLOAD_DOCUMENT_BY_ID_URI = "/client-api/v1/documents/download?documentId=12";
    @Mock
    DocumentServiceImpl documentService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = standaloneSetup(new DocumentResource(documentService))
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    @DisplayName("Get all documents should OK")
    void testGetAllDocuments_shouldOK() throws Exception {
        Mockito.when(documentService.getAllDocuments(null, null, null, PageRequest.of(0, 20))).thenReturn(new PageImpl<>(List.of()));
        mockMvc.perform(get(GET_ALL_DOCUMENTS_URI))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get document detail by ID should OK")
    void testGetDocumentDetailById_shouldOK() throws Exception {
        Mockito.when(documentService.getDocumentById(1L)).thenReturn(mockDocumentResponse());
        mockMvc.perform(get(GET_DOCUMENT_BY_ID_URI).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Delete document by ID should OK")
    void testDeleteDocumentById_shouldOK() throws Exception {
        Mockito.doNothing().when(documentService).deleteById(1L);
        mockMvc.perform(delete(GET_DOCUMENT_BY_ID_URI).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Upload document should OK")
    void testUploadDocument_shouldOK() throws Exception {
        Mockito.doNothing().when(documentService).uploadFiles(Mockito.any(), Mockito.any());
        mockMvc.perform(post(UPLOAD_DOCUMENT_URI).contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Download document by ID should OK")
    void testDownloadDocumentById_shouldOK() throws Exception {
        Mockito.when(documentService.downloadById(12L)).thenReturn(new ExtraInfoResource(null, null));
        mockMvc.perform(get(DOWNLOAD_DOCUMENT_BY_ID_URI).contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(status().isOk());
    }

    private DocumentResponse mockDocumentResponse() {
        var result = new DocumentResponse();
        result.setId(1L);
        result.setCategory(DocumentCategoryEnum.TEMPLATE);
        result.setStatus(DocumentStatusEnum.ACTIVE);
        result.setFileName("test.txt");
        return result;
    }
}
