package server.api.termterm.dto.term;

import lombok.*;
import server.api.termterm.domain.bookmark.BookmarkStatus;

import java.util.Objects;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TermSimpleDto {
    private Long id;
    private String name;
    private String description;
    private BookmarkStatus bookmarked;

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, bookmarked);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if(!(o instanceof TermSimpleDto))
            return false;

        TermSimpleDto termSimpleDto = (TermSimpleDto) o;

        return this.id.equals(termSimpleDto.id) &&
                this.name.equals(termSimpleDto.name) &&
                this.description.equals(termSimpleDto.description) &&
                this.bookmarked.name().equals(termSimpleDto.bookmarked.name());
    }
}
