package server.api.termterm.dto.term;

import lombok.Builder;
import lombok.Getter;
import server.api.termterm.domain.bookmark.BookmarkStatus;

@Getter
@Builder
public class TermSimpleDto {
    private Long id;
    private String name;
    private String description;
    private BookmarkStatus bookmarked;
}
