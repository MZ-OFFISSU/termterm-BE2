package server.api.termterm.dto.term;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import server.api.termterm.domain.bookmark.BookmarkStatus;
import server.api.termterm.domain.term.Term;
import server.api.termterm.dto.comment.CommentDto;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class TermDto {
    private Long id;
    private String name;
    private String description;
    private String source;

    private List<String> categories;
    private List<CommentDto> comments;
    private BookmarkStatus bookmarked;

    @JsonIgnore
    private Term term;

    public TermDto(Long id, String name, String description, String source, BookmarkStatus bookmarked, Term term){
        this.id = id;
        this.name = name;
        this.description = description;
        this.source = source;
        this.bookmarked = bookmarked;
        this.term = term;
    }
}
