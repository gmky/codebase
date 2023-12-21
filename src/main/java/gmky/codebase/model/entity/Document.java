package gmky.codebase.model.entity;

import gmky.codebase.enumeration.DocumentCategoryEnum;
import gmky.codebase.enumeration.DocumentStatusEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "DOCUMENTS")
public class Document extends AbstractAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "FILE_NAME")
    private String fileName;

    @Column(name = "FILE_PATH")
    private String filePath;

    @Column(name = "FILE_TYPE")
    private String fileType;

    @Column(name = "CATEGORY")
    @Enumerated(EnumType.STRING)
    private DocumentCategoryEnum category;

    @Column(name = "REF_ID")
    private Long refId;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private DocumentStatusEnum status;

    @Column(name = "DELETED_AT")
    private Instant deletedAt;
}
