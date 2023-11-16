package gmky.codebase.model.entity;

import gmky.codebase.enumeration.APSTypeEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Setter
@Getter
@Table(name = "ASSIGNABLE_PERMISSION_SET")
public class AssignablePermissionSet extends AbstractAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private APSTypeEnum type;

    @ManyToMany
    @JoinTable(name = "ASSIGNABLE_PERMISSION_SET_ITEM",
            joinColumns = @JoinColumn(name = "APS_ID"),
            inverseJoinColumns = @JoinColumn(name = "FP_ID"))
    private Set<FunctionPrivilege> functionPrivileges;

    @OneToMany(mappedBy = "assignablePermissionSet")
    private Set<JobRole> jobRoles;
}
