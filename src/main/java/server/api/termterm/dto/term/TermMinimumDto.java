package server.api.termterm.dto.term;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import server.api.termterm.domain.bookmark.BookmarkStatus;

@Getter
@Builder
@AllArgsConstructor
public class TermMinimumDto {
    private Long id;
    private String name;
    private BookmarkStatus bookmarked;
}
