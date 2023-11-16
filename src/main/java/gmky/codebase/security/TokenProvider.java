package gmky.codebase.security;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;

import java.util.Map;
import java.util.function.Function;

public interface TokenProvider {
    String generateToken(String username, Map<String, Object> contexts);

    Authentication getAuthentication(String token);

    <T> T extractClaim(String token, Function<Claims, T> claimResolver);
}
