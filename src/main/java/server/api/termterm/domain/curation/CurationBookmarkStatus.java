package server.api.termterm.domain.curation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CurationBookmarkStatus {
    YES("Y"),
    NO("N"),
    ;

    private final String status;
}
