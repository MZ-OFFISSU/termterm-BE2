package server.api.termterm.domain.bookmark;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BookmarkStatus {
    YES("Y"),
    NO("N"),
    ;

    private final String status;
}
