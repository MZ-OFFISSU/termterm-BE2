package server.api.termterm.dto.dailyTerm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.api.termterm.domain.bookmark.BookmarkStatus;
import server.api.termterm.domain.term.DailyTerm;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DailyTermAndStatusDto {
    private DailyTerm dailyTerm;
    private BookmarkStatus status;
}
