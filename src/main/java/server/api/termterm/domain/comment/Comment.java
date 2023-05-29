package server.api.termterm.domain.comment;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.api.termterm.domain.basetime.BaseTimeEntity;
import server.api.termterm.domain.member.Member;
import server.api.termterm.domain.report.Report;
import server.api.termterm.domain.term.Term;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    private Long id;

    private String content;
    private String source;

    private Integer likeCnt;

    @Enumerated(EnumType.STRING)
    private CommentStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TERM_ID")
    private Term term;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentLike> commentLikes;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reports;

    @PrePersist
    public void initLikeCnt(){
        this.likeCnt = 0;
    }

    public void addLike(){
        this.likeCnt++;
    }

    public void subLike(){
        this.likeCnt--;
    }

    public void accept(){
        this.status = CommentStatus.ACCEPTED;
    }
}
