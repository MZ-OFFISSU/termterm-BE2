package server.api.termterm.domain.comment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommentLikeStatus {
    YES("Y"),
    NO("N"),
    ;

    private final String status;
}
