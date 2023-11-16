package gmky.codebase.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class TokenProviderImpl implements TokenProvider {
    @Value("${application.security.base64-secret}")
    private String base64Secret;

    @Value("${spring.application.name}")
    private String applicationName;

    private final UserDetailsService userDetailsService;

    @Override
    public String generateToken(String username, Map<String, Object> contexts) {
        return Jwts.builder()
                .claims(contexts)
                .issuer(applicationName)
                .subject(username)
                .signWith(getSigningKey())
                .compact();
    }

    @Override
    public Authentication getAuthentication(String token) {
        var username = extractClaim(token, Claims::getSubject);
        var user = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(username, token, user.getAuthorities());
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        Claims claims = extractClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(base64Secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
