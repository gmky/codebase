package gmky.codebase.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.Resource;

@Getter
@Setter
@AllArgsConstructor
public class ExtraInfoResource {
    private String fileName;
    private Resource resource;
}
