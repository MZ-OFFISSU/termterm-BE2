package server.api.termterm.domain.comment;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.api.termterm.domain.BaseTimeEntity;
import server.api.termterm.domain.member.Member;
import server.api.termterm.domain.report.Report;
import server.api.termterm.domain.term.Term;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    private Long id;

    private String content;
    private String source;
    private Integer likeCnt = 0;

    @Enumerated(EnumType.STRING)
    private CommentStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TERM_ID")
    private Term term;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentLike> commentLikes = new ArrayList<>();

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reports = new ArrayList<>();

    @Builder
    public Comment(String content, String source, CommentStatus status, Term term, Member member) {
        this.content = content;
        this.source = source;
        this.status = status;
        this.term = term;
        this.member = member;
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