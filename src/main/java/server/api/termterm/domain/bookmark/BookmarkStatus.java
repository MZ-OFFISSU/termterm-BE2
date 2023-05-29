package server.api.termterm.domain.bookmark;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BookmarkStatus {
    YES("YES"),
    NO("NO"),
    ;

    private final String status;
}
