package gmky.codebase.model.entity;

import gmky.codebase.enumeration.UserStatusEnum;
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
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "USERS")
public class User extends AbstractAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private UserStatusEnum status;

    @Column(name = "FULL_NAME")
    private String fullName;

    @Column(name = "ACTIVATED_AT")
    private Instant activatedAt;

    @Column(name = "DEACTIVATED_AT")
    private Instant deactivatedAt;

    @Column(name = "RESET_AT")
    private Instant resetAt;

    @Column(name = "EXPIRED_AT")
    private Instant expiredAt;

    @ManyToMany
    @JoinTable(name = "ASSIGNED_FUNCTION_GROUP", joinColumns = @JoinColumn(name = "USER_ID"), inverseJoinColumns = @JoinColumn(name = "JOB_ROLE_ID"))
    private Set<JobRole> jobRoles;
}
