package server.api.termterm.domain.category;


import lombok.Getter;
import lombok.NoArgsConstructor;
import server.api.termterm.domain.curation.Curation;
import server.api.termterm.domain.term.Term;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CATEGORY_ID")
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "categories")
    private Set<Term> terms;

    @ManyToMany(mappedBy = "categories")
    private List<Curation> curations;
}
