package server.api.termterm.domain.term;

import lombok.Getter;
import lombok.NoArgsConstructor;
import server.api.termterm.domain.bookmark.TermBookmark;
import server.api.termterm.domain.category.Category;
import server.api.termterm.domain.comment.Comment;
import server.api.termterm.domain.curation.Curation;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Term {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TERM_ID")
    private Long id;

    private String name;

    @Column(columnDefinition = "LONGTEXT")
    private String description;
    private String source;

    @ManyToMany
    @JoinTable(name = "TERM_CATEGORY",
            joinColumns = @JoinColumn(name = "TERM_ID"),
            inverseJoinColumns = @JoinColumn(name = "CATEGORY_ID"))
    private List<Category> categories;

    @OneToMany(mappedBy = "term", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @ManyToMany(mappedBy = "terms")
    private List<Curation> curations;

    @OneToMany(mappedBy = "term", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TermBookmark> termBookmarks;

    @OneToMany(mappedBy = "term", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DailyTerm> dailyTerms;

}
