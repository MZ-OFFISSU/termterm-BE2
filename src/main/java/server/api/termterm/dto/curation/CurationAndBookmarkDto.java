package server.api.termterm.dto.curation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import server.api.termterm.domain.bookmark.BookmarkStatus;
import server.api.termterm.domain.curation.Curation;

@Getter
@AllArgsConstructor
public class CurationAndBookmarkDto {
    private Curation curation;
    private BookmarkStatus bookmarked;
}
