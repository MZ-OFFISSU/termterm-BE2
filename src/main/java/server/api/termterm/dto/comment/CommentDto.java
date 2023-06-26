package server.api.termterm.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private String content;
    private Integer likeCnt;
    private String authorName;
    private String authorJob;
    private String authorProfileImageUrl;
    private String createdDate;
    private String source;
}
