package server.api.termterm.domain.comment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommentStatus {
    ACCEPTED("A"),
    DECISION_IN_PROCESS("D"),
    REPORTED("R"),
    ;

    private final String status;
}
