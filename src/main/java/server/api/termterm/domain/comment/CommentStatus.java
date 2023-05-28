package server.api.termterm.domain.comment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommentStatus {
    ACCEPTED("ACCEPTED"),
    WAITING("WAITING"),
    REPORTED("REPORTED"),
    ;

    private final String status;
}
