package gmky.codebase.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Named;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MapperUtil {
    private final PasswordEncoder passwordEncoder;

    @Named("hashPassword")
    public String hashPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}
