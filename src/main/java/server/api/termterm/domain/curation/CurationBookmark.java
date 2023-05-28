package server.api.termterm.domain.curation;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.api.termterm.domain.member.Member;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor
public class CurationBookmark {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "CURATION_BOOKMARK_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "CURATION_ID")
    private Curation curation;

    @Enumerated(EnumType.STRING)
    private CurationBookmarkStatus status;

    @Builder
    public CurationBookmark(Member member, Curation curation){
        this.member = member;
        this.curation = curation;
        this.status = CurationBookmarkStatus.YES;
    }

    public void unbookmark(){
        this.status = CurationBookmarkStatus.NO;
    }

    public void bookmark(){
        this.status = CurationBookmarkStatus.YES;
    }
}
