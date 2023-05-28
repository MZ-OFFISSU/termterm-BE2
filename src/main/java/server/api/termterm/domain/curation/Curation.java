package server.api.termterm.domain.curation;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.api.termterm.domain.category.Category;
import server.api.termterm.domain.term.Term;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
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
    private Set<Category> categories = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "CURATION_TERM",
            joinColumns = @JoinColumn(name = "CURATION_ID"),
            inverseJoinColumns = @JoinColumn(name = "TERM_ID"))
    private List<Term> terms = new ArrayList<>();

    public void synchronizeCnt(){
        this.cnt = this.terms.size();
    }

    @Builder
    public Curation(String title, String description, String thumbnail, List<Tag> tags, Set<Category> categories, List<Term> terms){
        this.title = title;
        this.description = description;
        this.thumbnail = thumbnail;
        this.tags = tags;
        this.categories = categories;
        this.terms = terms;
    }

}
