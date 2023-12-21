package gmky.codebase.mapper;

import gmky.codebase.api.model.DocumentResponse;
import gmky.codebase.api.model.UploadFileReq;
import gmky.codebase.model.entity.Document;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DocumentMapper extends EntityMapper<DocumentResponse, Document> {
    Document toEntity(UploadFileReq req);
}
