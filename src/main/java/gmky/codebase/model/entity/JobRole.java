package gmky.codebase.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Set;

@Entity
@Setter
@Getter
@Table(name = "JOB_ROLE")
public class JobRole extends AbstractAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "START_AT")
    private Instant startAt;

    @Column(name = "END_AT")
    private Instant endAt;

    @Column(name = "APS_ID")
    private Long apsId;

    @ManyToOne
    @JoinTable(name = "JOB_ROLE_ITEM", joinColumns = @JoinColumn(name = "JOB_ROLE_ID"), inverseJoinColumns = @JoinColumn(name = "FP_ID"))
    private AssignablePermissionSet assignablePermissionSet;

    @ManyToMany
    @JoinTable(name = "JOB_ROLE_ITEM", joinColumns = @JoinColumn(name = "JOB_ROLE_ID"), inverseJoinColumns = @JoinColumn(name = "FP_ID"))
    private Set<FunctionPrivilege> functionPrivileges;

    @ManyToMany(mappedBy = "jobRoles")
    private Set<User> users;
}
