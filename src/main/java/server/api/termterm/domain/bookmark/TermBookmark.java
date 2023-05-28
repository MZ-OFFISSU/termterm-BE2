package server.api.termterm.domain.bookmark;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.api.termterm.domain.member.Member;
import server.api.termterm.domain.term.Term;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor
public class TermBookmark {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "TERM_BOOKMARK_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "TERM_ID")
    private Term term;

    @Enumerated(EnumType.STRING)
    private BookmarkStatus status;

    @Builder
    public TermBookmark(Member member, Term term){
        this.member = member;
        this.term = term;
        this.status = BookmarkStatus.YES;
    }

    public void unbookmark(){
        this.status = BookmarkStatus.NO;
    }

    public void bookmark(){
        this.status = BookmarkStatus.YES;
    }
}