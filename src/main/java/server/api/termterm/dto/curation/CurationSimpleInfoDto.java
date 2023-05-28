package server.api.termterm.dto.curation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import server.api.termterm.domain.bookmark.BookmarkStatus;

@Getter
@Builder
@AllArgsConstructor
public class CurationSimpleInfoDto {
    private Long curationId;
    private String title;
    private String description;
    private Integer cnt;
    private BookmarkStatus bookmarked;
}
