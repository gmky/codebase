package gmky.codebase.repository;

import gmky.codebase.enumeration.DocumentCategoryEnum;
import gmky.codebase.enumeration.DocumentStatusEnum;
import gmky.codebase.model.entity.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    @Query("SELECT doc FROM Document doc WHERE (:category IS NULL OR doc.category = :category) AND (:status IS NULL OR doc.status = :status) AND (:refId IS NULL OR doc.refId = :refId)")
    Page<Document> getAllDocuments(@Param("category") DocumentCategoryEnum category, @Param("status") DocumentStatusEnum status, @Param("refId") Long refId, Pageable pageable);
}
