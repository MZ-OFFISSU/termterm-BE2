package server.api.termterm.domain.category;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CategoryType {
    PM(1L),
    MARKETING(2L),
    DEVELOPMENT(3L),
    DESIGN(4L),
    BUSINESS(5L),
    IT(6L);

    private final Long id;
}
