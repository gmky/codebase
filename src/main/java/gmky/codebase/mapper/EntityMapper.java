package gmky.codebase.mapper;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

public interface EntityMapper <D, E> {
    D toDto(E entity);

    E toEntity(D dto);

    List<D> toDto(List<E> entities);

    List<E> toEntity(List<D> dtos);

    default OffsetDateTime toOffsetDateTime(Instant instant) {
        if (instant == null) return null;
        return OffsetDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    default Instant toInstant(OffsetDateTime offsetDateTime) {
        if (offsetDateTime == null) return null;
        return offsetDateTime.toInstant();
    }
}
