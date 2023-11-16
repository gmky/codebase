package gmky.codebase.util;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@UtilityClass
public class SecurityUtil {
    public static String getCurrentUsername() {
        var ctxHolder = SecurityContextHolder.getContext();
        var authentication = ctxHolder.getAuthentication();
        if (authentication == null) return null;
        var principal = authentication.getPrincipal();
        if (principal instanceof String username) {
            return username;
        } else if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }
        return null;
    }
}
