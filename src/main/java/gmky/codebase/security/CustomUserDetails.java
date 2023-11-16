package gmky.codebase.security;

import gmky.codebase.enumeration.UserStatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final String username;
    private final UserStatusEnum status;
    private final Instant expiredAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return Instant.now().isAfter(expiredAt);
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserStatusEnum.ACTIVE.equals(status);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return UserStatusEnum.ACTIVE.equals(status);
    }
}
