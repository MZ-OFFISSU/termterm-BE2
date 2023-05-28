package server.api.termterm.domain.curation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.api.termterm.domain.bookmark.CurationBookmark;
import server.api.termterm.domain.category.Category;
import server.api.termterm.domain.term.Term;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Curation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CURATION_ID")
    private Long id;

    private String title;
    private String description;
    private Integer cnt;
    private String thumbnail;

    @OneToMany(mappedBy = "curation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CurationBookmark> curationBookmarks = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "CURATION_TAG",
            joinColumns = @JoinColumn(name = "CURATION_ID"),
            inverseJoinColumns = @JoinColumn(name = "TAG_ID"))
    private List<Tag> tags = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "CURATION_CATEGORY",
            joinColumns = @JoinColumn(name = "CURATION_ID"),
            inverseJoinColumns = @JoinColumn(name = "CATEGORY_ID"))
    private List<Category> categories = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "CURATION_TERM",
            joinColumns = @JoinColumn(name = "CURATION_ID"),
            inverseJoinColumns = @JoinColumn(name = "TERM_ID"))
    private List<Term> terms = new ArrayList<>();

    public void synchronizeCnt(){
        this.cnt = this.terms.size();
    }
}
