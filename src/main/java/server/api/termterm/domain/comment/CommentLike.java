package server.api.termterm.domain.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import server.api.termterm.domain.member.Member;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_LIKE_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMMENT_ID")
    private Comment comment;

    @Enumerated(EnumType.STRING)
    private CommentLikeStatus status;

    public CommentLike(Member member, Comment comment, CommentLikeStatus status){
        this.member = member;
        this.comment = comment;
        this.status = status;
    }

    public void like(){
        this.status = CommentLikeStatus.YES;
    }

    public void dislike(){
        this.status = CommentLikeStatus.NO;
    }

}
