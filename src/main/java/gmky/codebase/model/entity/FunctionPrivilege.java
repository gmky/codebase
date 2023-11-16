package gmky.codebase.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "FUNCTION_PRIVILEGE")
public class FunctionPrivilege extends AbstractAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "BUSINESS_FUNCTION_CODE", updatable = false)
    private String bfCode;

    @Column(name = "RESOURCE_CODE", updatable = false)
    private String resourceCode;

    @Column(name = "PRIVILEGE_CODE", updatable = false)
    private String privilegeCode;

    @Column(name = "BUSINESS_FUNCTION_ID")
    private Long bfId;

    @Column(name = "PRIVILEGE_ID")
    private Long privilegeId;

    @ManyToMany(mappedBy = "functionPrivileges")
    private Set<AssignablePermissionSet> assignablePermissionSets;

    @ManyToMany(mappedBy = "functionPrivileges")
    private Set<JobRole> jobRoles;
}
