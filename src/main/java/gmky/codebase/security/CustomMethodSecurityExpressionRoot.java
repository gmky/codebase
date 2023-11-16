package gmky.codebase.security;

import gmky.codebase.model.entity.FunctionPrivilege;
import gmky.codebase.model.entity.JobRole;
import gmky.codebase.model.entity.User;
import gmky.codebase.repository.UserRepository;
import gmky.codebase.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class CustomMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {
    private final UserRepository userRepository;
    private Object filterObject;
    private Object returnObject;

    public CustomMethodSecurityExpressionRoot(Authentication authentication, UserRepository userRepository) {
        super(authentication);
        this.userRepository = userRepository;
    }

    public boolean checkPermission(String resourceCode, String functionCode, String[] privileges) {
        var username = SecurityUtil.getCurrentUsername();
        var userOptional = userRepository.findByUsernameIgnoreCase(username);
        if (userOptional.isEmpty()) return false;
        var user = userOptional.get();
        var allFunctionPrivileges = getAllFunctionPrivileges(user);
        var matchedFp = getMatchedFunctionPrivileges(allFunctionPrivileges, resourceCode, functionCode);
        var assignedPrivileges = getAllPrivilege(matchedFp);
        return assignedPrivileges.containsAll(Arrays.asList(privileges));
    }

    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public Object getFilterObject() {
        return this.filterObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public Object getReturnObject() {
        return this.returnObject;
    }

    @Override
    public Object getThis() {
        return this;
    }

    public List<FunctionPrivilege> getAllFunctionPrivileges(User user) {
        return user.getJobRoles()
                .stream()
                .filter(this::isNotExpired)
                .map(JobRole::getFunctionPrivileges)
                .flatMap(Collection::stream)
                .toList();
    }

    public Set<FunctionPrivilege> getMatchedFunctionPrivileges(List<FunctionPrivilege> assignedFunctionPrivileges, String resourceCode, String functionCode) {
        return assignedFunctionPrivileges.stream()
                .filter(item -> isMatched(item, functionCode, resourceCode))
                .collect(Collectors.toSet());
    }

    public Set<String> getAllPrivilege(Set<FunctionPrivilege> matchedFunctionPrivileges) {
        return matchedFunctionPrivileges.stream()
                .map(FunctionPrivilege::getPrivilegeCode)
                .collect(Collectors.toSet());
    }

    private boolean isMatched(FunctionPrivilege fp, String functionCode, String resourceCode) {
        return StringUtils.equalsIgnoreCase(fp.getBfCode(), functionCode)
                && StringUtils.equalsIgnoreCase(fp.getResourceCode(), resourceCode);
    }

    private boolean isNotExpired(JobRole jobRole) {
        var now = Instant.now();
        return now.isAfter(jobRole.getStartAt()) && now.isBefore(jobRole.getEndAt());
    }
}
