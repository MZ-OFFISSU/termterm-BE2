package server.api.termterm.domain.curation;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TAG_ID")
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "tags")
    private List<Curation> curations;

    public Tag(String name){
        this.name = name;
    }

}
